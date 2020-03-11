package com.sambudisp.muvi.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.sambudisp.muvi.FavWidget
import com.sambudisp.muvi.R
import com.sambudisp.muvi.adapter.FavAdapter
import com.sambudisp.muvi.database.helper.FavHelper
import com.sambudisp.muvi.database.helper.MappingHelper
import com.sambudisp.muvi.model.localstorage.FavModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private var content = ArrayList<FavModel>()
    private var contentX = ArrayList<FavModel>()
    private lateinit var adapter: FavAdapter
    private lateinit var favHelper: FavHelper

    private val widgetItem = ArrayList<Bitmap>()
    //private val widgetItem = ArrayList<String>()
    private var isi: String? = null

    override fun onCreate() {
        favHelper = FavHelper.getInstance(context)
        if (!favHelper.isOpen()) favHelper.open()
    }

    override fun onDataSetChanged() {
        try {
            val cursor = favHelper.queryAll()
            val resultCursor = MappingHelper.mapCursorToArrayList(cursor)
            if (resultCursor.size > 0) {
                GlobalScope.launch(Dispatchers.IO) {
                    val deferredFavs = async(Dispatchers.IO) {
                        val cursorSearch = favHelper.queryAll()
                        MappingHelper.mapCursorToArrayList(cursorSearch)
                    }
                    val fav = deferredFavs.await()
                    if (fav.size > 0) {
                        content.clear()
                        content = fav
                        for (i in 0 until content.size) {
                            val poster = content[i].poster.toString()
                            val bitmap = Glide.with(context)
                                .asBitmap()
                                .load("https://image.tmdb.org/t/p/w342" + poster)
                                .submit()
                                .get()
                            widgetItem.add(bitmap)
                        }
                    } else {
                        content.clear()
                    }
                }
            } else {
                content.clear()
            }
        } catch (e: IllegalStateException) {
        }
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.item_widget_fav)
        rv.setImageViewBitmap(R.id.img_banner_widget_fav, widgetItem[position])

        val extras = bundleOf(
            FavWidget.EXTRA_ITEM to position
        )

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.img_banner_widget_fav, fillInIntent)
        return rv

//         val rv = RemoteViews(context.packageName, R.layout.item_widget_fav)
//        rv.setTextViewText(R.id.txt_banner_widget_fav, widgetItem[position])
//
//        val extras = bundleOf(
//            FavWidget.EXTRA_ITEM to position
//        )
//
//        val fillInIntent = Intent()
//        fillInIntent.putExtras(extras)
//        rv.setOnClickFillInIntent(R.id.txt_banner_widget_fav, fillInIntent)
//        return rv
//
    }

    override fun getCount(): Int = widgetItem.size

    override fun onDestroy() {}

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}

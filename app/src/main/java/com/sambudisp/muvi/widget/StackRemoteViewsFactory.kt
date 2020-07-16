package com.sambudisp.muvi.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.sambudisp.muvi.FavWidget
import com.sambudisp.muvi.R
import com.sambudisp.muvi.database.helper.FavHelper
import com.sambudisp.muvi.database.helper.MappingHelper

class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private lateinit var favHelper: FavHelper
    private val widgetItem = ArrayList<Bitmap>()

    override fun onCreate() {
        favHelper = FavHelper.getInstance(context)
        if (!favHelper.isOpen()) favHelper.open()
    }

    override fun onDataSetChanged() {
        favHelper = FavHelper.getInstance(context)
        if (!favHelper.isOpen()) favHelper.open()

        val identityToken = Binder.clearCallingIdentity()
        Binder.restoreCallingIdentity(identityToken)

        try {
            val cursorSearch = favHelper.queryAll()
            val cursor = MappingHelper.mapCursorToArrayList(cursorSearch)
            val fav = cursor
            if (fav.size > 0) {
                widgetItem.clear()
                for (i in 0 until fav.size) {
                    try {
                        val poster = fav[i].poster.toString()
                        val bitmap = Glide.with(context)
                            .asBitmap()
                            .load("https://image.tmdb.org/t/p/w342" + poster)
                            .submit()
                            .get()
                        widgetItem.add(bitmap)
                    } catch (e: Exception) {
                        Log.d("ErrorWidget", e.message.toString())
                        widgetItem.add(
                            BitmapFactory.decodeResource(
                                context.resources,
                                R.drawable.ic_broken_image_black_24dp
                            )
                        )
                    }
                }
            }
        } catch (e: IllegalStateException) {
            Log.d("ErrorWidget", "${e.message}")
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
    }

    override fun getCount(): Int = widgetItem.size

    override fun onDestroy() {}

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}

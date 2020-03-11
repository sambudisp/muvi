package com.sambudisp.muvi.activity

import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sambudisp.muvi.R
import com.sambudisp.muvi.adapter.DeletedListener
import com.sambudisp.muvi.adapter.FavAdapter
import com.sambudisp.muvi.database.DatabaseContract
import com.sambudisp.muvi.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.sambudisp.muvi.database.helper.FavHelper
import com.sambudisp.muvi.database.helper.MappingHelper
import com.sambudisp.muvi.model.localstorage.FavModel
import kotlinx.android.synthetic.main.activity_fav.*
import kotlinx.android.synthetic.main.fragment_fav_movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavActivity : AppCompatActivity(), DeletedListener {

    private lateinit var adapter: FavAdapter
    private lateinit var favHelper: FavHelper

    private var fav: FavModel? = null

    companion object {
        val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)
        setupView()

        fav = FavModel()

        rv_fav.layoutManager = LinearLayoutManager(this)
        rv_fav.setHasFixedSize(true)
        adapter = FavAdapter(this)
        rv_fav.adapter = adapter

        favHelper = FavHelper.getInstance(this)
        if (!favHelper.isOpen()) favHelper.open()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavAsync()
            }
        }
        contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadFavAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavModel>(FavActivity.EXTRA_STATE)
            if (list != null) {
                adapter.listFav = list
            }
        }
    }

    private fun setupView() {
        supportActionBar?.title = getString(R.string.title_fav)
        //val sectionsPagerAdapter = FavSectionsPagerAdapter(this, supportFragmentManager)
        //view_pager_fav_category.adapter = sectionsPagerAdapter
        //tab_fav_category.setupWithViewPager(view_pager_fav_category)
        supportActionBar?.elevation = 0f
    }

    private fun loadFavAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar.visibility = View.VISIBLE
            val deferredFavs = async(Dispatchers.IO) {
                //val cursor = favHelper.queryByCategory("movie")
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressbar.visibility = View.INVISIBLE
            val fav = deferredFavs.await()
            if (fav.size > 0) {
                adapter.listFav.clear()
                adapter.listFav = fav
                adapter.notifyDataSetChanged()
            } else {
                adapter.listFav.clear()
                adapter.notifyDataSetChanged()
                showSnackbarMessage(getString(R.string.data_empty))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(FavActivity.EXTRA_STATE, adapter.listFav)
    }

    override fun onDeleted() {
        loadFavAsync()
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_fav_movie, message, Snackbar.LENGTH_LONG).show()
    }

}

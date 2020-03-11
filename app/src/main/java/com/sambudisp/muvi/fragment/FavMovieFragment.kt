package com.sambudisp.muvi.fragment

import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sambudisp.muvi.R
import com.sambudisp.muvi.activity.FavActivity
import com.sambudisp.muvi.adapter.DeletedListener
import com.sambudisp.muvi.adapter.FavAdapter
import com.sambudisp.muvi.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.sambudisp.muvi.database.helper.FavHelper
import com.sambudisp.muvi.database.helper.MappingHelper
import com.sambudisp.muvi.model.localstorage.FavModel
import kotlinx.android.synthetic.main.fragment_fav_movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavMovieFragment : Fragment(), DeletedListener {

    private lateinit var adapter: FavAdapter
    private lateinit var favHelper: FavHelper

    private var fav: FavModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fav_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fav = FavModel()

        rv_fav_movie.layoutManager = LinearLayoutManager(context)
        rv_fav_movie.setHasFixedSize(true)
        adapter = FavAdapter(this)
        rv_fav_movie.adapter = adapter

        favHelper = FavHelper.getInstance(activity?.applicationContext!!)
        if (!favHelper.isOpen()) favHelper.open()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavAsync()
            }
        }
        activity?.contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadFavAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavModel>(FavActivity.EXTRA_STATE)
            if (list != null) {
                adapter.listFav = list
            }
        }
    }

    private fun loadFavAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar_fav_movie.visibility = View.VISIBLE
            val deferredFavs = async(Dispatchers.IO) {
                //val cursor = favHelper.queryByCategory("movie")
                val uriWithCategory = Uri.parse(CONTENT_URI.toString() + "/category")//+ fav?.category)
                val cursor = activity?.contentResolver?.query(uriWithCategory, null, null, null, null)
                //val cursor = activity?.contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressbar_fav_movie.visibility = View.INVISIBLE
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

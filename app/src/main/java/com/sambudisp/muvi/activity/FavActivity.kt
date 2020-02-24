package com.sambudisp.muvi.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sambudisp.muvi.R
import com.sambudisp.muvi.adapter.DeletedListener
import com.sambudisp.muvi.adapter.FavAdapter
import com.sambudisp.muvi.database.helper.FavHelper
import com.sambudisp.muvi.database.helper.MappingHelper
import com.sambudisp.muvi.model.localStorage.FavModel
import kotlinx.android.synthetic.main.activity_fav.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavActivity : AppCompatActivity(), DeletedListener {

    private lateinit var adapter: FavAdapter
    private lateinit var favHelper: FavHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)

        supportActionBar?.title = getString(R.string.title_fav)
        rv_fav.layoutManager = LinearLayoutManager(this)
        rv_fav.setHasFixedSize(true)
        adapter = FavAdapter(this, this)
        rv_fav.adapter = adapter

        favHelper = FavHelper.getInstance(applicationContext)
        if (!favHelper.isOpen()) favHelper.open()

        if (savedInstanceState == null) {
            loadFavAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavModel>(EXTRA_STATE)
            if (list != null) {
                adapter.listFav = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFav)
    }

    private fun loadFavAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar.visibility = View.VISIBLE
            val deferredFavs = async(Dispatchers.IO) {
                val cursor = favHelper.queryAll()
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

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_fav, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDeleted() {
        loadFavAsync()
    }
}

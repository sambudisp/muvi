package com.sambudisp.muvi.fragment


import android.os.Bundle
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
import com.sambudisp.muvi.database.helper.FavHelper
import com.sambudisp.muvi.database.helper.MappingHelper
import com.sambudisp.muvi.model.localstorage.FavModel
import kotlinx.android.synthetic.main.fragment_fav_tv_show.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavTvShowFragment : Fragment(), DeletedListener {

    private lateinit var adapter: FavAdapter
    private lateinit var favHelper: FavHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fav_tv_show, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_fav_tv.layoutManager = LinearLayoutManager(context)
        rv_fav_tv.setHasFixedSize(true)
        adapter = FavAdapter(this)
        rv_fav_tv.adapter = adapter

        favHelper = FavHelper.getInstance(activity?.applicationContext!!)
        if (!favHelper.isOpen()) favHelper.open()

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
            progressbar_fav_tv.visibility = View.VISIBLE
            val deferredFavs = async(Dispatchers.IO) {
                val cursor = favHelper.queryByCategory("tv")
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressbar_fav_tv.visibility = View.INVISIBLE
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
        Snackbar.make(rv_fav_tv, message, Snackbar.LENGTH_LONG).show()
    }

}

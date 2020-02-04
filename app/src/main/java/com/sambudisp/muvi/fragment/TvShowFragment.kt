package com.sambudisp.muvi.fragment


import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sambudisp.muvi.activity.ContentDetailActivity
import com.sambudisp.muvi.ContentModel
import com.sambudisp.muvi.R
import com.sambudisp.muvi.model.response.MovieResponseResult
import com.sambudisp.muvi.model.response.TVResponseResult
import com.sambudisp.muvi.viewModel.MovieViewModel
import com.sambudisp.muvi.viewModel.TvViewModel
import kotlinx.android.synthetic.main.fragment_tv_show.*

class TvShowFragment : Fragment(), TvListListener {

    private lateinit var tvAdapter: TvShowListAdapter
    private lateinit var viewModel: TvViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvAdapter = TvShowListAdapter(this)
        tvAdapter.notifyDataSetChanged()
        setupRecylerView()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(TvViewModel::class.java)
        viewModel.setListMovie()

        viewModel.getListMovie().observe(this, Observer {
            if (it != null){
                tvAdapter.setData(it)
            }
        })
    }

    private fun setupRecylerView() {
        rv_tv_show_list?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tvAdapter
        }
    }

    override fun onClick(tv: TVResponseResult) {
        val intent = Intent(context, ContentDetailActivity::class.java)
        intent.putExtra(ContentDetailActivity.EXTRA_DATA, tv)
        startActivity(intent)
    }

}

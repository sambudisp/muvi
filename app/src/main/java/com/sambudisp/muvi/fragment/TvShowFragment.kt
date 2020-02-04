package com.sambudisp.muvi.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sambudisp.muvi.activity.ContentDetailActivity
import com.sambudisp.muvi.R
import com.sambudisp.muvi.adapter.TvListListener
import com.sambudisp.muvi.adapter.TvShowListAdapter
import com.sambudisp.muvi.model.response.TVResponseResult
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
        viewModel.setListTv()
        isLoading(true)

        viewModel.getListTv().observe(this, Observer {
            if (it != null){
                tvAdapter.setData(it)
                isLoading(false)
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
        val bundle = Bundle()
        bundle.putString(ContentDetailActivity.EXTRA_DATA, tv.id)
        bundle.putString(ContentDetailActivity.EXTRA_TYPE, "tv")
        val intent = Intent(context, ContentDetailActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            pb_tv.visibility = View.VISIBLE
            rv_tv_show_list.visibility = View.GONE
        } else {
            pb_tv.visibility = View.GONE
            rv_tv_show_list.visibility = View.VISIBLE
        }
    }

}

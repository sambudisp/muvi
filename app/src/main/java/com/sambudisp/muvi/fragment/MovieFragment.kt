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
import com.sambudisp.muvi.viewModel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment(), MovieListListener {

    private lateinit var movieAdapter: MovieListAdapter
    private lateinit var viewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieListAdapter(this)
        movieAdapter.notifyDataSetChanged()
        setupRecylerView()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MovieViewModel::class.java)
        viewModel.setListMovie()

        viewModel.getListMovie().observe(this, Observer {
            if (it != null){
                movieAdapter.setData(it)
            }
        })
    }

    private fun setupRecylerView() {
        rv_movie_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieAdapter
        }
    }


    override fun onClick(movie: MovieResponseResult) {
        val intent = Intent(context, ContentDetailActivity::class.java)
        intent.putExtra(ContentDetailActivity.EXTRA_DATA, movie)
        startActivity(intent)
    }


}

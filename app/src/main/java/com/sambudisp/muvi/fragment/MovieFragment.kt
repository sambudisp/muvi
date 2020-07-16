package com.sambudisp.muvi.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sambudisp.muvi.R
import com.sambudisp.muvi.activity.ContentDetailActivity
import com.sambudisp.muvi.adapter.MovieFavListener
import com.sambudisp.muvi.adapter.MovieListAdapter
import com.sambudisp.muvi.adapter.MovieListListener
import com.sambudisp.muvi.model.response.MovieResponseResult
import com.sambudisp.muvi.viewModel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_movie.*


class MovieFragment : Fragment(), MovieListListener, MovieFavListener {

    private lateinit var movieAdapter: MovieListAdapter
    private lateinit var viewModel: MovieViewModel

    private var keyword: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieListAdapter(this, this)
        movieAdapter.notifyDataSetChanged()
        setupRecylerView()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MovieViewModel::class.java)
        viewModel.setListMovie(keyword)
        isLoading(true)

        viewModel.getListMovie().observe(this, Observer {
            if (it != null) {
                movieAdapter.setData(it)
                isLoading(false)
            }
        })

        edt_search_movie.clearFocus()
        btn_search_movie?.setOnClickListener {
            if (edt_search_movie.text.isNullOrBlank())
            onSearchClick(null)
            else onSearchClick(edt_search_movie.text.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MovieViewModel::class.java)
        viewModel.setListMovie(keyword)
        isLoading(true)
        viewModel.getListMovie().observe(this, Observer {
            if (it != null) {
                movieAdapter.setData(it)
                isLoading(false)
            }
        })
    }

    private fun onSearchClick(query: String?) {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MovieViewModel::class.java)
        viewModel.setListMovie(query)
        isLoading(true)
        viewModel.getListMovie().observe(this, Observer {
            if (it != null) {
                movieAdapter.setData(it)
                isLoading(false)
            }
        })
    }

    private fun setupRecylerView() {
        rv_movie_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieAdapter
        }
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            pb_movie.visibility = View.VISIBLE
            rv_movie_list.visibility = View.GONE
        } else {
            pb_movie.visibility = View.GONE
            rv_movie_list.visibility = View.VISIBLE
        }
    }

    override fun onClick(movie: MovieResponseResult) {
        val bundle = Bundle()
        bundle.putString(ContentDetailActivity.EXTRA_DATA, movie.id)
        bundle.putString(ContentDetailActivity.EXTRA_TYPE, "movie")
        val intent = Intent(context, ContentDetailActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onFavClick(movie: MovieResponseResult) {
        //save id ke lokal db
    }


}

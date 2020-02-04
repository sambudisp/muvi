package com.sambudisp.muvi.sub2


import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sambudisp.muvi.ContentDetailActivity
import com.sambudisp.muvi.ContentModel
import com.sambudisp.muvi.R
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment(), OnMovieListClick {

    private val listOfMovie = arrayListOf<ContentModel>()
    private val movieAdapter = MovieListAdapter(listOfMovie, this)

    private lateinit var dataTitle: Array<String>
    private lateinit var dataDescription: Array<String>
    private lateinit var dataRate: Array<String>
    private lateinit var dataRestriction: Array<String>
    private lateinit var dataPrice: Array<String>
    private lateinit var dataIsPromo: Array<String>
    private lateinit var dataCateogry: Array<String>
    private lateinit var dataVideo: Array<String>
    private lateinit var dataPoster: TypedArray

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareData()
        addItemData()
        setupRecylerView()
    }

    private fun setupRecylerView() {
        rv_movie_list?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieAdapter
        }
    }

    private fun addItemData() {
        for (position in dataTitle.indices){
            val movie = ContentModel(
                dataPoster.getResourceId(position, -1),
                dataTitle[position],
                dataDescription[position],
                dataRate[position],
                dataRestriction[position],
                dataPrice[position],
                dataIsPromo[position].toBoolean(),
                dataCateogry[position],
                dataVideo[position],
                getString(R.string.movie)
            )
            listOfMovie.add(movie)
        }
        movieAdapter.notifyDataSetChanged()
    }

    private fun prepareData() {
        dataPoster = resources.obtainTypedArray(R.array.data_poster_movie)
        dataTitle = resources.getStringArray(R.array.data_title_movie)
        dataDescription = resources.getStringArray(R.array.data_description_movie)
        dataRate = resources.getStringArray(R.array.data_rate_movie)
        dataRestriction = resources.getStringArray(R.array.data_restriction_movie)
        dataPrice = resources.getStringArray(R.array.data_price_movie)
        dataIsPromo = resources.getStringArray(R.array.data_is_promo_movie)
        dataCateogry = resources.getStringArray(R.array.data_category_movie)
        dataVideo = resources.getStringArray(R.array.data_video_movie)
    }

    override fun onClick(movie: ContentModel) {
        val intent = Intent(context, ContentDetailActivity::class.java)
        intent.putExtra(ContentDetailActivity.EXTRA_DATA, movie)
        startActivity(intent)
    }

}

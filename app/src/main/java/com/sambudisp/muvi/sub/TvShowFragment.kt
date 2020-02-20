package com.sambudisp.muvi.sub


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
import kotlinx.android.synthetic.main.fragment_tv_show.*

class TvShowFragment : Fragment(), OnTvShowClick {

    private val listOfMovie = arrayListOf<ContentModel>()
    private val movieAdapter = TvShowListAdapter(listOfMovie, this)

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
        return inflater.inflate(R.layout.fragment_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareData()
        addItemData()
        setupRecylerView()
    }

    private fun setupRecylerView() {
        rv_tv_show_list?.apply {
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
                getString(R.string.tv_show)
            )
            listOfMovie.add(movie)
        }
        movieAdapter.notifyDataSetChanged()
    }

    private fun prepareData() {
        dataPoster = resources.obtainTypedArray(R.array.data_poster_tv)
        dataTitle = resources.getStringArray(R.array.data_title_tv)
        dataDescription = resources.getStringArray(R.array.data_description_tv)
        dataRate = resources.getStringArray(R.array.data_rate_tv)
        dataRestriction = resources.getStringArray(R.array.data_restriction_tv)
        dataPrice = resources.getStringArray(R.array.data_price_tv)
        dataIsPromo = resources.getStringArray(R.array.data_is_promo_tv)
        dataCateogry = resources.getStringArray(R.array.data_category_tv)
        dataVideo = resources.getStringArray(R.array.data_video_tv)
    }

    override fun onClick(tvShow: ContentModel) {
        val intent = Intent(context, ContentDetailActivity::class.java)
        intent.putExtra(ContentDetailActivity.EXTRA_DATA, tvShow)
        startActivity(intent)
    }
}

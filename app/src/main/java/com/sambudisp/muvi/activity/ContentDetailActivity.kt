package com.sambudisp.muvi.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sambudisp.muvi.R
import com.sambudisp.muvi.model.response.MovieDetailResponse
import com.sambudisp.muvi.model.response.TvDetailResponse
import com.sambudisp.muvi.viewModel.ContentDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_detail.*

class ContentDetailActivity : AppCompatActivity() {

    private var fabPlay: FloatingActionButton? = null
    private var btnOrder: TextView? = null
    private lateinit var viewModel: ContentDetailViewModel
    private var id: String? = null
    private var type: String? = null
    private var homepageUrl: String? = null

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_TYPE = "extra_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_movie_detail)

        initComponent()
        setupButton()

        if (intent.extras != null) {
            val bundle = intent?.extras
            id = bundle?.getString(EXTRA_DATA)
            type = bundle?.getString(EXTRA_TYPE)
        } else {
            Toast.makeText(this, getString(R.string.err), Toast.LENGTH_LONG).show()
        }

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            ContentDetailViewModel::class.java
        )
        viewModel.setDetail(id, type)
        isLoading(true)

        if (type == "tv") {
            viewModel.getDetailTv().observe(this, Observer {
                if (it != null) {
                    setupContentDataTv(it)
                    isLoading(false)
                }
            })
        } else {
            viewModel.getDetailMovie().observe(this, Observer {
                if (it != null) {
                    setupContentDataMovie(it)
                    isLoading(false)
                }
            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupContentDataTv(data: TvDetailResponse) {
        this.title = data.title
        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500" + data.backdrop_path)
            .error(R.drawable.ic_broken_image_black_24dp)
            .into(img_poster)
        tv_title.text = data.title
        tv_rate.text = data.vote_average + "/10"
        tv_category.text = getString(R.string.released_at) +
                " ${data?.release_date}"
        tv_description.text = if (data.overview == "" || data.overview == "") getString(
            R.string.null_desc
        ) else data.overview
        if (data.number_of_seasons == null || data.number_of_seasons == "") {
            tv_season_episode.visibility = View.GONE
        } else {
            tv_season_episode.visibility = View.VISIBLE
            tv_season_episode.text =
                "${data.number_of_seasons} " + getString(R.string.season) + " | " + "${data.number_of_episodes} Episode/" + getString(
                    R.string.season
                )
        }
        homepageUrl = data.homepage
    }

    private fun setupContentDataMovie(data: MovieDetailResponse) {
        this.title = data.title
        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500" + data.backdrop_path)
            .error(R.drawable.ic_broken_image_black_24dp)
            .into(img_poster)
        tv_title.text = data.title
        tv_rate.text = data.vote_average + "/10"
        tv_category.text = getString(R.string.released_at) +
                " ${data?.release_date}"
        tv_description.text = if (data.overview == "" || data.overview == "") getString(
            R.string.null_desc
        ) else data.overview
        if (data.number_of_seasons == null || data.number_of_seasons == "") {
            tv_season_episode.visibility = View.GONE
        } else {
            tv_season_episode.visibility = View.VISIBLE
            tv_season_episode.text =
                "${data.number_of_seasons} " + getString(R.string.season) + " | " + "${data.number_of_episodes} Episode/" + getString(
                    R.string.season
                )
        }
        homepageUrl = data.homepage
    }

    private fun initComponent() {
        fabPlay = findViewById(R.id.fab_play)
        btnOrder = findViewById(R.id.btn_order)
    }

    private fun setupButton() {
        fabPlay?.setOnClickListener {
            if (homepageUrl != null) {
                val uri = Uri.parse(homepageUrl)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.err), Toast.LENGTH_LONG).show()
            }
        }

        btnOrder?.setOnClickListener {
            Toast.makeText(this, getString(R.string.order_isb_processing), Toast.LENGTH_LONG).show()
        }
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            pb_detail.visibility = View.VISIBLE
        } else {
            pb_detail.visibility = View.GONE
        }
    }
}

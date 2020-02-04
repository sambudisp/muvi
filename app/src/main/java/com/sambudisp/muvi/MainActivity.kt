package com.sambudisp.muvi

import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.sambudisp.muvi.sub2.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MovieListener {

    private var movieAdapter = MovieAdapter(this, this)
    private var movie = arrayListOf<ContentModel>()

    private lateinit var dataTitle: Array<String>
    private lateinit var dataDescription: Array<String>
    private lateinit var dataRate: Array<String>
    private lateinit var dataRestriction: Array<String>
    private lateinit var dataPrice: Array<String>
    private lateinit var dataIsPromo: Array<String>
    private lateinit var dataCateogry: Array<String>
    private lateinit var dataVideo: Array<String>
    private lateinit var dataPoster: TypedArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()
        setupListView()
        prepareData()
        addItemData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.tb_setting) {
            changeLanguage()
            return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeLanguage() {
        val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        startActivity(intent)
    }

    private fun setupView() {
        this.title = getString(R.string.main_title)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager_category.adapter = sectionsPagerAdapter
        tab_category.setupWithViewPager(view_pager_category)
        supportActionBar?.elevation = 0f
    }

    private fun setupListView() {
        val lvMovie : ListView? = findViewById(R.id.lv_movie)
        lvMovie?.adapter = movieAdapter
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
            this.movie.add(movie)
        }
        movieAdapter.movie = movie
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

    override fun onDetailClicked(content: ContentModel) {
        val intent = Intent(this, ContentDetailActivity::class.java)
        intent.putExtra(ContentDetailActivity.EXTRA_DATA, content)
        startActivity(intent)
    }
}

package com.sambudisp.muvi.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sambudisp.muvi.R
import com.sambudisp.muvi.fragment.FavSectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_fav.*

class FavActivity : AppCompatActivity() {

    companion object {
        val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)
        setupView()
    }

    private fun setupView() {
        supportActionBar?.title = getString(R.string.title_fav)
        val sectionsPagerAdapter = FavSectionsPagerAdapter(this, supportFragmentManager)
        view_pager_fav_category.adapter = sectionsPagerAdapter
        tab_fav_category.setupWithViewPager(view_pager_fav_category)
        supportActionBar?.elevation = 0f
    }

}

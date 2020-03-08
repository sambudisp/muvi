package com.sambudisp.muvi.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.sambudisp.muvi.R
import com.sambudisp.muvi.database.helper.FavHelper
import com.sambudisp.muvi.fragment.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var favHelper: FavHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()

        favHelper = FavHelper.getInstance(applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
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
        } else if (id == R.id.tb_fav) {
            goFavList()
            return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goFavList() {
        val intent = Intent(this, FavActivity::class.java)
        startActivity(intent)
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
}

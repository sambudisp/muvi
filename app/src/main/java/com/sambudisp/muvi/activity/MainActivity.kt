package com.sambudisp.muvi.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.sambudisp.muvi.BuildConfig.API_KEY
import com.sambudisp.muvi.DailyOpenAppReceiver
import com.sambudisp.muvi.MuviApp
import com.sambudisp.muvi.R
import com.sambudisp.muvi.database.helper.FavHelper
import com.sambudisp.muvi.fragment.SectionsPagerAdapter
import com.sambudisp.muvi.model.response.MovieResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var favHelper: FavHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()

        favHelper = FavHelper.getInstance(applicationContext)

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(root_layout.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
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
        if (id == R.id.tb_go_setting) {
            goSetting()
            //changeLanguage()
            return super.onOptionsItemSelected(item)
        } else if (id == R.id.tb_fav) {
            goFavList()
            return super.onOptionsItemSelected(item)
        } else if (id == R.id.tb_reminder) {
            //setReminderOnOff()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goSetting() {
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }

    private fun goFavList() {
        val intent = Intent(this, FavActivity::class.java)
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

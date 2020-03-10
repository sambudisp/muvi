package com.sambudisp.muvi.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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

    private lateinit var dailyNotif: DailyOpenAppReceiver
    private lateinit var menuPushNotif: MenuItem

    private lateinit var newMovie : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()

        favHelper = FavHelper.getInstance(applicationContext)

        dailyNotif = DailyOpenAppReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        menuPushNotif = menu?.findItem(R.id.tb_reminder)!!
        if (
            dailyNotif.isAlarmSetOpenApp(this) ||
            dailyNotif.isAlarmSetNewMovie(this)
        ) {
            menuPushNotif.setTitle(getString(R.string.setting_reminder_off))
        } else {
            menuPushNotif.setTitle(getString(R.string.setting_reminder_on))
        }
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
        } else if (id == R.id.tb_reminder) {
            setReminderOnOff()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setReminderOnOff() {
        if (
            dailyNotif.isAlarmSetOpenApp(this) ||
            dailyNotif.isAlarmSetNewMovie(this)
        ) {
            dailyNotif.cancelAlarmOpenApp(this)
            dailyNotif.cancelAlarmNewMovie(this)
            menuPushNotif.setTitle(getString(R.string.setting_reminder_on))
        } else {
            dailyNotif.setRepeatingAlarmOpenApp(
                this, DailyOpenAppReceiver.TYPE_REPEATING_OPEN_APP,
                "07:00", "Banyak film favoritmu. Cuss cek di app sekarang!"
            )
            dailyNotif.setRepeatingAlarmNewMovie(
                this, DailyOpenAppReceiver.TYPE_REPEATING_OPEN_APP,
                "08:00", "Film Baru Hari Ini"
            )
            menuPushNotif.setTitle(getString(R.string.setting_reminder_off))
        }
    }

    private fun getNewMovie(){
        var language = "id-ID"
        if (Locale.getDefault().displayLanguage.toString() != "Indonesia") language = "en-US"

        MuviApp.apiService
            .movie(API_KEY, language)
            .enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    response.code().let {
                        try {
                            if (it == 200) {
                                response.body()?.let {
                                    newMovie = it.results[0].title.toString()
                                }
                            } else {
                                Log.d(
                                    "onSuccessErr",
                                    "Code : ${it} | Msg : ${response.errorBody()?.string()}"
                                )
                            }
                        } catch (e: Exception) {
                            Log.d("Exception", e.message.toString())
                        }
                    }
                }
            })
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

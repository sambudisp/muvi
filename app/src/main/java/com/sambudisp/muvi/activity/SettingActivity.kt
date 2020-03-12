package com.sambudisp.muvi.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.sambudisp.muvi.DailyOpenAppReceiver
import com.sambudisp.muvi.R
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    private lateinit var dailyNotif: DailyOpenAppReceiver

    companion object {
        val KEY_OPEN_APP = "open_app"
        val KEY_NEW_MOVIE = "new_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar?.title = getString(R.string.title_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        dailyNotif = DailyOpenAppReceiver()

        checkReminder(KEY_OPEN_APP)
        checkReminder(KEY_NEW_MOVIE)
        setupButton()
    }

    private fun setupButton() {
        btn_stg_language?.setOnClickListener {
            changeLanguage()
        }

        btn_stg_notification_open_app?.setOnCheckedChangeListener { buttonView, isChecked ->
            setReminderOnOff(KEY_OPEN_APP)
        }

        btn_stg_notification_new_movie?.setOnCheckedChangeListener { buttonView, isChecked ->
            setReminderOnOff(KEY_NEW_MOVIE)
        }
    }


    private fun checkReminder(key: String?) {
        when (key) {
            KEY_OPEN_APP ->
                if (dailyNotif.isAlarmSetOpenApp(this)) {
                    btn_stg_notification_open_app.setText(getString(R.string.notif_is_on_open_app))
                    btn_stg_notification_open_app.isChecked = true
                } else {
                    btn_stg_notification_open_app.setText(getString(R.string.notif_is_off_open_app))
                    btn_stg_notification_open_app.isChecked = false
                }
            KEY_NEW_MOVIE ->
                if (dailyNotif.isAlarmSetNewMovie(this)) {
                    btn_stg_notification_new_movie.setText(getString(R.string.notif_is_on_new_movie))
                    btn_stg_notification_new_movie.isChecked = true
                } else {
                    btn_stg_notification_new_movie.setText(getString(R.string.notif_is_off_new_movie))
                    btn_stg_notification_new_movie.isChecked = false
                }
            else -> Log.d("Error", key.toString())
        }
    }

    private fun setReminderOnOff(key: String?) {
        when (key) {
            KEY_OPEN_APP ->
                if (dailyNotif.isAlarmSetOpenApp(this)) {
                    dailyNotif.cancelAlarmOpenApp(this)
                    btn_stg_notification_open_app.setText(getString(R.string.notif_is_off_open_app))
                    btn_stg_notification_open_app.isChecked = false
                } else {
                    dailyNotif.setRepeatingAlarmOpenApp(
                        this, DailyOpenAppReceiver.TYPE_REPEATING_OPEN_APP,
                        "07:00", "Banyak film favoritmu. Cuss cek di app sekarang!"
                    )
                    btn_stg_notification_open_app.setText(getString(R.string.notif_is_on_open_app))
                    btn_stg_notification_open_app.isChecked = true
                }
            KEY_NEW_MOVIE ->
                if (dailyNotif.isAlarmSetNewMovie(this)) {
                    dailyNotif.cancelAlarmNewMovie(this)
                    btn_stg_notification_new_movie.setText(getString(R.string.notif_is_off_new_movie))
                    btn_stg_notification_new_movie.isChecked = false
                } else {
                    dailyNotif.setRepeatingAlarmNewMovie(
                        this, DailyOpenAppReceiver.TYPE_REPEATING_NEW_MOVIE,
                        "08:00", "Film Baru Hari Ini"
                    )
                    btn_stg_notification_new_movie.setText(getString(R.string.notif_is_on_new_movie))
                    btn_stg_notification_new_movie.isChecked = true
                }
            else -> Log.d("Error", key.toString())
        }
        checkReminder(KEY_OPEN_APP)
        checkReminder(KEY_NEW_MOVIE)
    }


    private fun changeLanguage() {
        val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        startActivity(intent)
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
}

package com.sambudisp.muvi.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.sambudisp.muvi.DailyOpenAppReceiver
import com.sambudisp.muvi.R
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    private lateinit var dailyNotif: DailyOpenAppReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar?.title = getString(R.string.title_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        dailyNotif = DailyOpenAppReceiver()

        checkReminder()
        setupButton()
    }

    private fun setupButton() {
        btn_stg_language?.setOnClickListener {
            changeLanguage()
        }

        btn_stg_notification?.setOnCheckedChangeListener { buttonView, isChecked ->
            setReminderOnOff()
        }
    }


    private fun checkReminder() {
        if (
            dailyNotif.isAlarmSetOpenApp(this) ||
            dailyNotif.isAlarmSetNewMovie(this)
        ) {
            btn_stg_notification.setText(getString(R.string.notif_is_on))
            btn_stg_notification.isChecked = true
        } else {
            btn_stg_notification.setText(getString(R.string.notif_is_off))
            btn_stg_notification.isChecked = false
        }
    }

    private fun setReminderOnOff() {
        if (dailyNotif.isAlarmSetOpenApp(this) || dailyNotif.isAlarmSetNewMovie(this)) {
            dailyNotif.cancelAlarmOpenApp(this)
            dailyNotif.cancelAlarmNewMovie(this)
            btn_stg_notification.setText(getString(R.string.notif_is_off))
            btn_stg_notification.isChecked = false
        } else {
            dailyNotif.setRepeatingAlarmOpenApp(
                this, DailyOpenAppReceiver.TYPE_REPEATING_OPEN_APP,
                "07:00", "Banyak film favoritmu. Cuss cek di app sekarang!"
            )
            dailyNotif.setRepeatingAlarmNewMovie(
                this, DailyOpenAppReceiver.TYPE_REPEATING_OPEN_APP,
                "08:00", "Film Baru Hari Ini"
            )
            btn_stg_notification.setText(getString(R.string.notif_is_on))
            btn_stg_notification.isChecked = true
        }
        checkReminder()
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

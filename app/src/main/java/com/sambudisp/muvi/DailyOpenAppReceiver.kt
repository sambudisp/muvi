package com.sambudisp.muvi

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.sambudisp.muvi.activity.MainActivity
import com.sambudisp.muvi.model.NotifModel
import com.sambudisp.muvi.model.response.MovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DailyOpenAppReceiver : BroadcastReceiver() {

    var idNotification = 0
    val stackNotif = ArrayList<NotifModel>()

    companion object {
        const val TYPE_REPEATING_OPEN_APP = "RepeatingAlarmOpenApp"
        const val TYPE_REPEATING_NEW_MOVIE = "RepeatingAlarmNewMovie"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        const val EXTRA_TYPE_OPEN_APP = "type_open_app"
        const val EXTRA_TYPE_NEW_MOVIE = "type_new_movie"

        private const val ID_REPEATING_DAILY_OPEN_APP = 101
        private const val ID_REPEATING_DAILY_NEW_MOVIE = 102

        private const val TIME_FORMAT = "HH:mm"

        val NOTIF_GROUP = "movie_group"
        val MAX_NOTIFICATION = 0
    }

    private var newMovie: String? = null

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val title = "Muvi"
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val notifIdOpenApp = ID_REPEATING_DAILY_OPEN_APP
        val notifIdNewMovie = ID_REPEATING_DAILY_NEW_MOVIE

        val calendar = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = dateFormat.format(calendar).toString()

        Log.d("HASILNYATYPE", type.toString())
        if (type == EXTRA_TYPE_OPEN_APP) {
            showAlarmNotification(context, title, message, notifIdOpenApp)
        } else if (type == EXTRA_TYPE_NEW_MOVIE) {
            MuviApp.apiService
                .movieToday(BuildConfig.API_KEY, date, date)
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
                                    response.body()?.let { movie ->
                                        for (i in 0 until movie.results.size) {
                                            val result =
                                                "${context.getString(R.string.new_movie)} ${movie.results[i].title.toString()}"
                                            Log.d("HASILNYA", "${idNotification} ${result}")
                                            showAlarmNotificationNewMovie(
                                                context,
                                                title,
                                                movie.results[i].title.toString(),
                                                idNotification
                                            )
                                        }
                                        Log.d(
                                            "TOTAL",
                                            movie.results.size.toString() + " | " + idNotification.toString()
                                        )
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
    }

    fun setRepeatingAlarmOpenApp(context: Context, type: String, time: String, message: String) {
        if (isDateInvalid(time, TIME_FORMAT)) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyOpenAppReceiver::class.java)
        intent.putExtra(EXTRA_TYPE, EXTRA_TYPE_OPEN_APP)
        intent.putExtra(EXTRA_MESSAGE, message)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent =
            PendingIntent.getBroadcast(context, ID_REPEATING_DAILY_OPEN_APP, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(
            context,
            context.getString(R.string.notif_is_on_open_app),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun setRepeatingAlarmNewMovie(context: Context, type: String, time: String, message: String) {
        if (isDateInvalid(time, TIME_FORMAT)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyOpenAppReceiver::class.java)
        intent.putExtra(EXTRA_TYPE, EXTRA_TYPE_NEW_MOVIE)
        intent.putExtra(EXTRA_MESSAGE, context.getString(R.string.new_movie) + " ${newMovie}")

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent =
            PendingIntent.getBroadcast(context, ID_REPEATING_DAILY_NEW_MOVIE, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(
            context,
            context.getString(R.string.notif_is_on_new_movie),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showAlarmNotification(
        context: Context,
        title: String?,
        message: String?,
        notifId: Int
    ) {

        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManager channel"

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                ID_REPEATING_DAILY_OPEN_APP,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_info)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    private fun showAlarmNotificationNewMovie(
        context: Context,
        title: String?,
        message: String?,
        notifId: Int
    ) {

        stackNotif.add(
            NotifModel(
                idNotification,
                title,
                message
            )
        )

        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManager channel"

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                ID_REPEATING_DAILY_NEW_MOVIE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder: NotificationCompat.Builder
        if (idNotification >= MAX_NOTIFICATION) {
            builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_info)
                .setContentTitle(title)
                .setContentText(context.getString(R.string.new_movie) + " - " + stackNotif[idNotification].message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(alarmSound)
                .setGroup(NOTIF_GROUP)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        } else {
            val inboxStyle = NotificationCompat.InboxStyle()
                .addLine(stackNotif[idNotification].message)
                .setBigContentTitle("${context.getString(R.string.new_movie)}")
//                .setSummaryText("${context.getString(R.string.new_movie)}")

            builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_info)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(alarmSound)
                .setGroupSummary(true)
                .setStyle(inboxStyle)
                .setGroup(NOTIF_GROUP)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
        idNotification++
    }

    fun cancelAlarmOpenApp(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyOpenAppReceiver::class.java)
        val requestCode = ID_REPEATING_DAILY_OPEN_APP
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(
            context,
            context.getString(R.string.notif_is_off_open_app),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun cancelAlarmNewMovie(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyOpenAppReceiver::class.java)
        val requestCode = ID_REPEATING_DAILY_NEW_MOVIE
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(
            context,
            context.getString(R.string.notif_is_off_new_movie),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun isAlarmSetOpenApp(context: Context): Boolean {
        val intent = Intent(context, DailyOpenAppReceiver::class.java)
        val requestCode = ID_REPEATING_DAILY_OPEN_APP
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE
        ) != null
    }

    fun isAlarmSetNewMovie(context: Context): Boolean {
        val intent = Intent(context, DailyOpenAppReceiver::class.java)
        val requestCode = ID_REPEATING_DAILY_NEW_MOVIE
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE
        ) != null
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }
}

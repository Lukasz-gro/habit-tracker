package com.example.habits.receivers

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.habits.R
import java.util.*

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        sendNotificationOnChannel1(context, intent)
    }

    private fun sendNotificationOnChannel1(context: Context, intent: Intent) {
        val habitEndTime = intent.getIntegerArrayListExtra("hourEnds")
        if (habitEndTime != null) {
            for (i in habitEndTime) {
                println("Wypisuje godzuny które przyszly $i")
            }
        }
        val hour = getHourOfTheDay()
        val toDoBeforeHour = habitEndTime?.filter {
            it / 100 <= hour
        }

        if (toDoBeforeHour != null) {
            for (i in toDoBeforeHour) {
                println("Wypisuje godzuny które zostały $i")
            }
        }
        val notification = NotificationCompat.Builder(
            context,
            "channel1"
        )
            .setSmallIcon(R.drawable.baseline_crisis_alert_24)
            .setContentTitle("Reminder")
            .setContentText("You have ${toDoBeforeHour?.size} habits to do!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("NOTIFICATION ERROR")
        }

        NotificationManagerCompat.from(context).notify(1, notification)
    }

    private fun getHourOfTheDay(): Int {
        val date = Date()
        val cal = Calendar.getInstance()
        cal.time = date
        val hours = cal.get(Calendar.HOUR_OF_DAY)
        println("Hours today, hours today $hours")
        return hours
    }
}
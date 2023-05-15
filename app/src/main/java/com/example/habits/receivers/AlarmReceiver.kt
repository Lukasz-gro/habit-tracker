package com.example.habits.receivers

import android.Manifest
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.habits.R
import com.example.habits.data.HabitRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val remainingHabits = HabitRoomDatabase.getDatabase(context).habitDao().remainingHabits(0, LocalDate.now().dayOfYear)
        var flowValue: Int
        runBlocking (Dispatchers.IO) {
            flowValue = remainingHabits.first()

        }
        sendNotificationOnChannel1(context, flowValue)
    }

    private fun sendNotificationOnChannel1(context: Context, remainingHabits: Int) {
        val notification = NotificationCompat.Builder(
            context,
            "channel1"
        )
            .setSmallIcon(R.drawable.baseline_crisis_alert_24)
            .setContentTitle("Reminder")
            .setContentText("You have $remainingHabits habits to do!")
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
}
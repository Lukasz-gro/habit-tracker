package com.example.habits.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.habits.R

class NotifyActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sendNotificationOnChannel1(this)
        finish()
    }

    private fun sendNotificationOnChannel1(context: Context) {
            val notification = NotificationCompat.Builder(
                context,
                "channel1"
            )
                .setSmallIcon(R.drawable.baseline_crisis_alert_24)
                .setContentTitle("Reminder")
                .setContentText("You have 5 habits to do!")
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

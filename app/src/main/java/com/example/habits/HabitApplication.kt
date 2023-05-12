package com.example.habits

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.habits.data.HabitRepository
import com.example.habits.data.HabitRoomDatabase

class HabitApplication : Application() {
    val database by lazy { HabitRoomDatabase.getDatabase(this) }
    val repository by lazy { HabitRepository(database.habitDao()) }
    val CHANNEL_1_ID = "channel1"
    val CHANNEL_2_ID = "channel2"

    override fun onCreate() {
        super.onCreate()

        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val channel1 = NotificationChannel(
            CHANNEL_1_ID,
            "main channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel1.description = "Channel 1 for notifications"

        val channel2 = NotificationChannel(
            CHANNEL_2_ID,
            "secondary channel",
            NotificationManager.IMPORTANCE_LOW
        )
        channel2.description = "Channel 2 for notifications"

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannels(listOf(channel1, channel2))
    }
}
package com.example.habits.alarms

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.habits.activities.HabitsScheduleActivity
import com.example.habits.receivers.AlarmReceiver
import java.util.Calendar

class NotificationAlarmScheduler(
    private val context: Context
) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager
    @SuppressLint("UnspecifiedImmutableFlag")
    private val alarmIntent = Intent(context, AlarmReceiver::class.java)

    fun schedule(remainingHabits: ArrayList<Int>) {
        println("To jest do zrobienia: ${remainingHabits.size}")
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 11)
            set(Calendar.MINUTE, 0)
        }

        alarmIntent.putExtra("remaining", remainingHabits[0])
        alarmIntent.putExtra("hourEnds", remainingHabits)

        val pendingIntent = alarmIntent.let {
                intent -> PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * 1,
            pendingIntent
        )
    }

    fun cancel() {
        val pendingIntent = alarmIntent.let {
                intent -> PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        alarmManager.cancel(pendingIntent)
    }
}
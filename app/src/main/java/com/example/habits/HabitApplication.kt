package com.example.habits

import android.app.Application
import com.example.habits.data.HabitRepository
import com.example.habits.data.HabitRoomDatabase

class HabitApplication : Application() {
    val database by lazy { HabitRoomDatabase.getDatabase(this) }
    val repository by lazy { HabitRepository(database.habitDao()) }
}
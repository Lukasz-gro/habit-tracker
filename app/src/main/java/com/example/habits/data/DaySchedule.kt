package com.example.habits.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daySchedule")
data class DaySchedule(
    @ColumnInfo(name = "day")
    val day: Int,
    @ColumnInfo(name = "habit")
    val habitId: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
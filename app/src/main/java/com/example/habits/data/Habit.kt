package com.example.habits.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habitList")
data class Habit (
    @ColumnInfo(name = "name")
    val habitName: String,
    @ColumnInfo(name = "category")
    val habitCategory: String,
    @ColumnInfo(name = "start_hour")
    val hourStart: Int,
    @ColumnInfo(name = "start_minute")
    val minuteStart: Int,
    @ColumnInfo(name = "duration")
    val duration: Int,
    @ColumnInfo(name = "score")
    val score: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
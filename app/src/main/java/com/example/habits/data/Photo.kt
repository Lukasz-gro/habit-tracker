package com.example.habits.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo (
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "path")
    val path: String,
    @ColumnInfo(name = "habitId")
    val habitId: Long,
    @ColumnInfo(name = "day_week")
    val day: Int,
    @ColumnInfo(name = "day_year")
    val doy: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
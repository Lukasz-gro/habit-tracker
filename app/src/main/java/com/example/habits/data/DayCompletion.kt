package com.example.habits.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dayCompletion")
data class DayCompletion (
    @ColumnInfo(name = "day")
    val day: Int,
    @ColumnInfo(name = "habitId")
    val habit: Long,
    @ColumnInfo(name = "done")
    val done: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

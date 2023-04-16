package com.example.habits.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(habit: Habit)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithId(habit: Habit) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHabitWithStatus(dayCompletion: DayCompletion)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDaySchedule(daySchedule: DaySchedule)

    @Update
    suspend fun update(habit: Habit)

    @Update
    fun update(dayCompletion: DayCompletion)

    @Delete
    suspend fun delete(habit: Habit)

    @Query("SELECT done from dayCompletion where day = :day and habitId = :habitId")
    fun getStatusOfHabit(day: Int, habitId: Long) : Flow<Boolean?>

    @Query("SELECT * from dayCompletion where day = :day and habitId = :habitId")
    fun getDayCompletion(day: Int, habitId: Long) : DayCompletion?

    @Query("SELECT * from habitList WHERE id = :id")
    fun getHabit(id: Long): Flow<Habit>

    @Query("DELETE FROM habitList WHERE id = :id")
    suspend fun deleteHabit(id: Long)

    @Query("SELECT name from habitList")
    fun getHabitsNames(): Flow<List<String>>

    @Query("SELECT * from habitList")
    fun getHabits(): Flow<List<Habit>>

    @Query("SELECT * from habitList where id IN (SELECT habit from daySchedule where day = :day)")
    fun getHabitsForDay(day: Int): Flow<List<Habit>>

    @Query("SELECT COUNT(*) from habitList where id IN (SELECT habit from daySchedule where day = :day)")
    fun getHabitCountForDay(day: Int): Flow<Int>

    @Query("SELECT COUNT(*) from dayCompletion where day = :day and done = true")
    fun countCompletedInDay(day: Int): Flow<Int>

//    @Query("SELECT c from (SELECT habitList.category as c, COUNT(habitList.category) as v from habitList " +
//            "JOIN daySchedule on habitList.id=daySchedule.habit " +
//            "WHERE daySchedule.day = :day " +
//            "GROUP BY habitList.category " +
//            "ORDER BY v DESC + " +
//            "LIMIT 1)")
    @Query("SELECT category from habitList LIMIT 1")
    fun getMostPopularCategory(): Flow<String?>

    @Query("DELETE from habitList")
    suspend fun deleteAll()

    @Query("DELETE from daySchedule")
    suspend fun deleteAllFromDaySchedule()

    @Query("DELETE from dayCompletion")
    suspend fun deleteAllFromDayCompletion()

    @Query("SELECT COUNT(*) from habitList where id IN " +
            "(SELECT habit from daySchedule where day = :dayOfWeek and habit NOT IN " +
            "(SELECT habitId from dayCompletion where day = :dayOfYear and done = true))")
    fun remainingHabits(dayOfWeek: Int, dayOfYear: Int): Flow<Int>

    @Query("SELECT SUM(score) from habitList where id in (SELECT habitId from dayCompletion where day = :day and done = true)")
    fun getScoreForDay(day: Int) : Flow<Int?>

    @Query("SELECT day from daySchedule WHERE habit = :id")
    fun getDaysOfHabit(id: Long): Flow<List<Int>>

    @Query("DELETE from daySchedule WHERE habit = :id")
    fun deleteFromDaySchedule(id: Long)
}
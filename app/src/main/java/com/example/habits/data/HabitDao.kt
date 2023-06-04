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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhoto(photo: Photo)

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

    @Query("SELECT COUNT(*) from habitList where category = :category and id IN (SELECT habit from daySchedule where day = :day)")
    fun getHabitCountForDayWithCategory(day: Int, category: String): Flow<Int>

    @Query("SELECT COUNT(*) from daySchedule where habit IN (SELECT id from habitList)")
    fun getHabitCountForWeek(): Flow<Int>

    @Query("SELECT COUNT(*) from daySchedule where habit IN (SELECT id from habitList WHERE category = :category)")
    fun getHabitCountForWeekWithCategory(category: String): Flow<Int>


    @Query("SELECT COUNT(*) from dayCompletion where day = :day and done = true")
    fun countCompletedInDay(day: Int): Flow<Int>

    @Query("SELECT COUNT(*) from dayCompletion where day = :day and done = true and habitId in (SELECT id from habitList where category = :category)")
    fun countCompletedInDayWithCategory(day: Int, category: String): Flow<Int>

    @Query("SELECT COUNT(*) from dayCompletion where day <= :day and day >= (:day-6) and done = true")
    fun countCompletedInWeek(day: Int): Flow<Int>

    @Query("SELECT COUNT(*) from dayCompletion where day <= :day and day >= (:day-6) and done = true and habitId in (SELECT id from habitList where category = :category)")
    fun countCompletedInWeekWithCategory(day: Int, category: String): Flow<Int>

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

    @Query("SELECT COUNT(*) from habitList where category = :category and id IN " +
            "(SELECT habit from daySchedule where day = :dayOfWeek and habit NOT IN " +
            "(SELECT habitId from dayCompletion where day = :dayOfYear and done = true))")
    fun remainingHabitsWithCategory(dayOfWeek: Int, dayOfYear: Int, category: String): Flow<Int>

    @Query("SELECT COUNT(*) from daySchedule where habit IN (SELECT id from habitList) and :dayOfWeek <= :dayOfYear")
    fun remainingHabitsForWeek(dayOfWeek: Int, dayOfYear: Int): Flow<Int>

    @Query("SELECT COUNT(*) from daySchedule where habit IN (SELECT id from habitList WHERE category = :category and :dayOfWeek <= :dayOfYear)")
    fun remainingHabitsForWeekWithCategory(dayOfWeek: Int, dayOfYear: Int, category: String): Flow<Int>

    @Query("SELECT * from habitList where id IN " +
            "(SELECT habit from daySchedule where day = :dayOfWeek and habit NOT IN " +
            "(SELECT habitId from dayCompletion where day = :dayOfYear and done = true))")
    fun remainingHabitsUntilHour(dayOfWeek: Int, dayOfYear: Int): List<Habit>

    @Query("SELECT SUM(score) from habitList where id in (SELECT habitId from dayCompletion where day = :day and done = true)")
    fun getScoreForDay(day: Int) : Flow<Int?>

    @Query("SELECT SUM(score) from habitList where category = :category and id in (SELECT habitId from dayCompletion where day = :day and done = true)")
    fun getScoreForDayWithCategory(day: Int, category: String): Flow<Int?>

    @Query("SELECT SUM(habitList.score) from dayCompletion " +
            "LEFT JOIN habitList ON dayCompletion.habitId=habitList.id " +
            "WHERE dayCompletion.day <= :day and dayCompletion.day >=(:day-6)")
    fun getScoreForWeek(day: Int) : Flow<Int?>

    @Query("SELECT SUM(habitList.score) from dayCompletion " +
            "LEFT JOIN habitList ON dayCompletion.habitId=habitList.id " +
            "WHERE dayCompletion.day <= :day and dayCompletion.day >=(:day-6) and habitList.category=:category")
    fun getScoreForWeekWithCategory(day: Int, category: String): Flow<Int?>

    @Query("SELECT day from daySchedule WHERE habit = :id")
    fun getDaysOfHabit(id: Long): Flow<List<Int>>

    @Query("DELETE from daySchedule WHERE habit = :id")
    fun deleteFromDaySchedule(id: Long)

    @Query("SELECT path from photos WHERE habitId = :habitId and day_week = :day and day_year = :dayOfYear")
    fun getPicturePath(habitId: Long, day: Int, dayOfYear: Int): Flow<String?>

    @Query("SELECT DISTINCT category FROM habitList")
    fun getCategoryList(): Flow<List<String>>

    @Query("SELECT * FROM photos")
    fun getAllPictures(): Flow<List<Photo>>
}
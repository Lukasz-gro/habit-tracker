package com.example.habits.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow

class HabitRepository(private val habitDao: HabitDao) {
    val allHabits: Flow<List<Habit>> = habitDao.getHabits()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(habit: Habit) {
        habitDao.insert(habit)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertDaySchedule(daySchedule: DaySchedule) {
        habitDao.insertDaySchedule(daySchedule)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertPhoto(photo: Photo) {
        habitDao.insertPhoto(photo)
    }

    @Suppress("RedundantSuspendModifier")
     fun getStatusOfHabit(day: Int, habitId: Long): LiveData<Boolean?> {
        return habitDao.getStatusOfHabit(day, habitId).asLiveData()
    }

    @Suppress("RedundantSuspendModifier")
    fun getRemainingHabits(day: Int, day2: Int): LiveData<Int> {
        return habitDao.remainingHabits(day, day2).asLiveData()
    }

    @Suppress("RedundantSuspendModifier")
    fun getRemainingHabitsUntilHour(day: Int, day2: Int): List<Habit> {
        return habitDao.remainingHabitsUntilHour(day, day2)
    }

    @Suppress("RedundantSuspendModifier")
    fun getDaysOfHabit(id: Long): LiveData<List<Int>> {
        return habitDao.getDaysOfHabit(id).asLiveData()
    }

    @Suppress("RedundantSuspendModifier")
    fun getScoreForADay(day: Int): LiveData<Int?> {
        return habitDao.getScoreForDay(day).asLiveData()
    }

    @Suppress("RedundantSuspendModifier")
    fun getDayCompletion(day: Int, habitId: Long): DayCompletion? {
        return habitDao.getDayCompletion(day, habitId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        habitDao.deleteAll()
        habitDao.deleteAllFromDayCompletion()
        habitDao.deleteAllFromDaySchedule()
    }

    @Suppress("RedundantSuspendModifier")
    fun getHabit(id: Long) : LiveData<Habit> {
        return habitDao.getHabit(id).asLiveData()
    }

    @Suppress("RedundantSuspendModifier")
    fun getHabitsForDay(day: Int) : LiveData<List<Habit>> {
        return habitDao.getHabitsForDay(day).asLiveData()
    }

    @Suppress("RedundantSuspendModifier")
    fun getHabitsCountForDay(day: Int) : LiveData<Int> {
        return habitDao.getHabitCountForDay(day).asLiveData()
    }

    @Suppress("RedundantSuspendModifier")
    fun countCompletedInDay(day: Int): LiveData<Int> {
        return habitDao.countCompletedInDay(day).asLiveData()
    }

    @Suppress("RedundantSuspendModifier")
    fun getMostPopularCategory(day: Int): LiveData<String?> {
        return habitDao.getMostPopularCategory().asLiveData()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteHabit(id: Long) {
        habitDao.deleteHabit(id)
    }

    fun insertWithId(habit: Habit): Long {
        return habitDao.insertWithId(habit)
    }

    fun insertHabitWithStatus(dayCompletion: DayCompletion) {
        habitDao.insertHabitWithStatus(dayCompletion)
    }

    fun updateDayCompletion(dayCompletion: DayCompletion) {
        habitDao.update(dayCompletion)
    }

    fun deleteFromDaySchedule(id: Long) {
        habitDao.deleteFromDaySchedule(id)
    }

    fun getPicturePath(habitId: Long, day: Int, dayOfYear: Int): LiveData<String?> {
        return habitDao.getPicturePath(habitId, day, dayOfYear).asLiveData()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateHabitInfo(habit: Habit) {
        habitDao.update(habit)
    }
}
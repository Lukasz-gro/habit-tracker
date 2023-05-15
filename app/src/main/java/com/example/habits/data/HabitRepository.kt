package com.example.habits.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow

class HabitRepository(private val habitDao: HabitDao) {
    val allHabits: Flow<List<Habit>> = habitDao.getHabits()

    @WorkerThread
    suspend fun insert(habit: Habit) {
        habitDao.insert(habit)
    }

    @WorkerThread
    suspend fun insertDaySchedule(daySchedule: DaySchedule) {
        habitDao.insertDaySchedule(daySchedule)
    }

    @WorkerThread
    suspend fun insertPhoto(photo: Photo) {
        habitDao.insertPhoto(photo)
    }

     fun getStatusOfHabit(day: Int, habitId: Long): LiveData<Boolean?> {
        return habitDao.getStatusOfHabit(day, habitId).asLiveData()
    }

    fun getRemainingHabits(day: Int, day2: Int): LiveData<Int> {
        return habitDao.remainingHabits(day, day2).asLiveData()
    }

    fun getRemainingHabitsWithCategory(day: Int, day2: Int, category: String): LiveData<Int> {
        return habitDao.remainingHabitsWithCategory(day, day2,category).asLiveData()
    }

    fun getRemainingHabitsForWeek(day: Int, day2: Int): LiveData<Int> {
        return habitDao.remainingHabitsForWeek(day, day2).asLiveData()
    }

    fun getRemainingHabitsForWeekWithCategory(day: Int, day2: Int, category: String): LiveData<Int> {
        return habitDao.remainingHabitsForWeekWithCategory(day, day2,category).asLiveData()
    }

    fun getRemainingHabitsUntilHour(day: Int, day2: Int): List<Habit> {
        return habitDao.remainingHabitsUntilHour(day, day2)
    }

    fun getDaysOfHabit(id: Long): LiveData<List<Int>> {
        return habitDao.getDaysOfHabit(id).asLiveData()
    }

    fun getScoreForADay(day: Int): LiveData<Int?> {
        return habitDao.getScoreForDay(day).asLiveData()
    }

    fun getScoreForADayWithCategory(day: Int, category: String): LiveData<Int?> {
        return habitDao.getScoreForDayWithCategory(day, category).asLiveData()
    }

    fun getScoreForWeek(day: Int): LiveData<Int?> {
        return habitDao.getScoreForWeek(day).asLiveData()
    }

    fun getScoreForWeekWithCategory(day: Int, category: String): LiveData<Int?> {
        return habitDao.getScoreForWeekWithCategory(day, category).asLiveData()
    }

    fun getDayCompletion(day: Int, habitId: Long): DayCompletion? {
        return habitDao.getDayCompletion(day, habitId)
    }

    @WorkerThread
    suspend fun deleteAll() {
        habitDao.deleteAll()
        habitDao.deleteAllFromDayCompletion()
        habitDao.deleteAllFromDaySchedule()
    }

    fun getHabit(id: Long) : LiveData<Habit> {
        return habitDao.getHabit(id).asLiveData()
    }

    fun getHabitsForDay(day: Int) : LiveData<List<Habit>> {
        return habitDao.getHabitsForDay(day).asLiveData()
    }

    fun getHabitsCountForDay(day: Int) : LiveData<Int> {
        return habitDao.getHabitCountForDay(day).asLiveData()
    }

    fun getHabitsCountForDayWithCategory(day: Int, category: String) : LiveData<Int> {
        return habitDao.getHabitCountForDayWithCategory(day, category).asLiveData()
    }

    fun getHabitsCountForWeek() : LiveData<Int> {
        return habitDao.getHabitCountForWeek().asLiveData()
    }

    fun getHabitsCountForWeekWithCategory(category: String) : LiveData<Int> {
        return habitDao.getHabitCountForWeekWithCategory(category).asLiveData()
    }

    fun countCompletedInDay(day: Int): LiveData<Int> {
        return habitDao.countCompletedInDay(day).asLiveData()
    }

    fun countCompletedInDayWithCategory(day: Int, category: String): LiveData<Int> {
        return habitDao.countCompletedInDayWithCategory(day, category).asLiveData()
    }

    fun countCompletedInWeek(day: Int): LiveData<Int> {
        return habitDao.countCompletedInWeek(day).asLiveData()
    }

    fun countCompletedInWeekWithCategory(day: Int, category: String): LiveData<Int> {
        return habitDao.countCompletedInWeekWithCategory(day, category).asLiveData()
    }

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

    fun getCategoryList(): LiveData<List<String>> {
        return habitDao.getCategoryList().asLiveData()
    }

    @WorkerThread
    suspend fun updateHabitInfo(habit: Habit) {
        habitDao.update(habit)
    }


}
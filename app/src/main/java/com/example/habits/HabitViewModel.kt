package com.example.habits

import androidx.lifecycle.*
import com.example.habits.data.*
import kotlinx.coroutines.launch
import java.util.*

class HabitViewModel(private val repository: HabitRepository) : ViewModel() {
    val allHabits: LiveData<List<Habit>> = repository.allHabits.asLiveData()

    fun addNewHabit(habit: Habit) {
        viewModelScope.launch {
            repository.insert(habit)
        }
    }

    fun insertDaySchedule(daySchedule: DaySchedule) {
        viewModelScope.launch {
            repository.insertDaySchedule(daySchedule)
        }
    }

    fun insertPhoto(photo: Photo) {
        viewModelScope.launch {
            repository.insertPhoto(photo)
        }
    }

    fun getHabitById(id: Long) : LiveData<Habit> {
       return repository.getHabit(id)
    }

    fun getStatusOfHabit(day: Int, habitId: Long): LiveData<Boolean?> {
        return repository.getStatusOfHabit(day, habitId)
    }

    fun getRemainingHabits(day: Int, day2: Int, category: String): LiveData<Int> {
        return if (category == "All categories") {
            repository.getRemainingHabits(day, day2)
        } else {
            repository.getRemainingHabitsWithCategory(day, day2, category)
        }
    }

    fun getRemainingHabitsForWeek(day: Int, day2: Int, category: String): LiveData<Int> {
        return if (category == "All categories") {
            repository.getRemainingHabitsForWeek(day, day2)
        } else {
            repository.getRemainingHabitsForWeekWithCategory(day, day2, category)
        }
    }

    fun getRemainingHabitsUntilHour(day: Int, day2: Int): List<Habit> {
        return repository.getRemainingHabitsUntilHour(day, day2)
    }

    fun getDaysOfHabit(id: Long): LiveData<List<Int>> {
        return repository.getDaysOfHabit(id)
    }

    fun getScoreForDay(day: Int, category: String): LiveData<Int?> {
        return if (category == "All categories") {
            repository.getScoreForADay(day)
        } else {
            repository.getScoreForADayWithCategory(day, category)
        }
    }

    fun getScoreForWeek(day: Int, category: String): LiveData<Int?> {
        return if (category == "All categories") {
            repository.getScoreForWeek(day)
        } else {
            repository.getScoreForWeekWithCategory(day, category)
        }
    }

    fun getDayCompletion(day: Int, habitId: Long): DayCompletion? {
        return repository.getDayCompletion(day, habitId)
    }

    fun getHabitsForDay(day: Int) : LiveData<List<Habit>> {
        return repository.getHabitsForDay(day)
    }

    fun getHabitsCountForDay(day: Int, category: String) : LiveData<Int> {
        return if (category == "All categories") {
            repository.getHabitsCountForDay(day)
        } else {
            repository.getHabitsCountForDayWithCategory(day, category)
        }
    }

    fun getHabitsCountForWeek(category: String) : LiveData<Int> {
        return if (category == "All categories") {
            repository.getHabitsCountForWeek()
        } else {
            repository.getHabitsCountForWeekWithCategory(category)
        }
    }

    fun countCompletedInDay(day: Int, category: String): LiveData<Int> {
        return if (category == "All categories") {
            repository.countCompletedInDay(day)
        } else {
            repository.countCompletedInDayWithCategory(day, category)
        }
    }

    fun countCompletedInWeek(day: Int, category: String): LiveData<Int> {
        return if (category == "All categories") {
            repository.countCompletedInWeek(day)
        } else {
            repository.countCompletedInWeekWithCategory(day, category)
        }
    }

    suspend fun deleteHabitById(id: Long) {
        return repository.deleteHabit(id)
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            repository.updateHabitInfo(habit)
        }
    }

    fun deleteAllHabits() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun insertWithId(habit: Habit): Long {
        return repository.insertWithId(habit)
    }

    fun insertHabitWithStatus(dayCompletion: DayCompletion) {
        repository.insertHabitWithStatus(dayCompletion)
    }

    fun updateDayCompletion(dayCompletion: DayCompletion) {
        repository.updateDayCompletion(dayCompletion)
    }

    fun deleteFromDaySchedule(id: Long) {
        repository.deleteFromDaySchedule(id)
    }

    fun getPicturePath(habitId: Long, day: Int, dayOfYear: Int): LiveData<String?> {
        return repository.getPicturePath(habitId, day, dayOfYear)
    }

    fun getCategoryList(): LiveData<List<String>> {
        return repository.getCategoryList()
    }

    fun getAllPictures(): LiveData<List<Photo>> {
        return repository.getAllPictures()
    }
}

class HabitViewModelFactory(private val repository: HabitRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}

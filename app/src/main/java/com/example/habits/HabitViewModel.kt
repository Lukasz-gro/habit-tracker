package com.example.habits

import androidx.lifecycle.*
import com.example.habits.data.DayCompletion
import com.example.habits.data.DaySchedule
import com.example.habits.data.Habit
import com.example.habits.data.HabitRepository
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

    fun getHabitById(id: Long) : LiveData<Habit> {
       return repository.getHabit(id)
    }

    fun getStatusOfHabit(day: Int, habitId: Long): LiveData<Boolean?> {
        return repository.getStatusOfHabit(day, habitId)
    }

    fun getRemainingHabits(day: Int, day2: Int): LiveData<Int> {
        return repository.getRemainingHabits(day, day2)
    }

    fun getDaysOfHabit(id: Long): LiveData<List<Int>> {
        return repository.getDaysOfHabit(id)
    }

    fun getScoreForDay(day: Int): LiveData<Int?> {
        return repository.getScoreForADay(day)
    }

    fun getDayCompletion(day: Int, habitId: Long): DayCompletion? {
        return repository.getDayCompletion(day, habitId)
    }

    fun getHabitsForDay(day: Int) : LiveData<List<Habit>> {
        return repository.getHabitsForDay(day)
    }

    fun getHabitsCountForDay(day: Int) : LiveData<Int> {
        return repository.getHabitsCountForDay(day)
    }

    fun countCompletedInDay(day: Int): LiveData<Int> {
        return repository.countCompletedInDay(day)
    }

    fun getMostPopularCategory(day: Int): LiveData<String?> {
        return repository.getMostPopularCategory(day)
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

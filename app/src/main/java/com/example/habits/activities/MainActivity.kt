package com.example.habits.activities

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.activity.viewModels
import com.example.habits.HabitApplication
import com.example.habits.HabitViewModel
import com.example.habits.HabitViewModelFactory
import com.example.habits.R
import com.example.habits.data.DaySchedule
import com.example.habits.data.Habit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val buttonState = mutableListOf(false, false, false, false)
    var count: Int = -1

    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory((application as HabitApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        val buttons = listOf(R.id.btnHabit1, R.id.btnHabit2, R.id.btnHabit3, R.id.btnHabit4)
//        CoroutineScope(Dispatchers.IO).launch {
//            habitViewModel.deleteAllHabits()
//        }

        habitViewModel.allHabits.observe(this) { selectedHabit ->
            count = selectedHabit.size
            if (count > 0) {
                val intent = Intent(this, HabitsScheduleActivity::class.java)
                this.startActivity(intent)
                finish()
            } else {

                setContentView(R.layout.choose_habits)

                val btnNext = findViewById<Button>(R.id.btnNext)
                btnNext.setOnClickListener {
                    val intent = Intent(this, HabitsScheduleActivity::class.java)
                    if(buttonState[0]) {
                        addHabitsForEducation()
                    }
                    if(buttonState[1]) {
                        addHabitsForPersonal()
                    }
                    if(buttonState[2]) {
                        addHabitsForSport()
                    }
                    if(buttonState[3]) {
                        addHabitsForPrograming()
                    }
                    this.startActivity(intent)
                    finish()
                }

                for (i in 0..3) {
                    val tempButton = findViewById<Button>(buttons[i])
                    tempButton.setOnClickListener {
                        chooseHabit(tempButton, i)
                    }
                }
            }
        }
    }

    private fun chooseHabit(btn: Button, id: Int) {
        if (!buttonState[id]) {
            btn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_button_selected))
            buttonState[id] = true
        } else {
            btn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_button))
            buttonState[id] = false
        }
    }

    private fun addHabitsForEducation() {
        CoroutineScope(Dispatchers.IO).launch {
            val id = habitViewModel.insertWithId(Habit("Reading", "Education", 8, 30, 45, 20))
            for (i in 0..4) {
                habitViewModel.insertDaySchedule(DaySchedule(i, id))
            }
            val id2 = habitViewModel.insertWithId(Habit("English", "Education", 9, 45, 20, 10))
            for (i in 0..4) {
                habitViewModel.insertDaySchedule(DaySchedule(i, id2))
            }
        }
    }

    private fun addHabitsForPersonal() {
        CoroutineScope(Dispatchers.IO).launch {
            val id = habitViewModel.insertWithId(Habit("Eat veggies", "Personal", 14, 12, 3, 5))
            for (i in 0..4) {
                habitViewModel.insertDaySchedule(DaySchedule(i, id))
            }
            val id2 = habitViewModel.insertWithId(Habit("Meditation", "Personal", 8, 30, 15, 12))
            for (i in 0..4) {
                habitViewModel.insertDaySchedule(DaySchedule(i, id2))
            }
        }
    }

    private fun addHabitsForSport() {
        CoroutineScope(Dispatchers.IO).launch {
            val id = habitViewModel.insertWithId(Habit("Gym", "Sport", 16, 0, 60, 25))
            for (i in 0..4) {
                habitViewModel.insertDaySchedule(DaySchedule(i, id))
            }
            val id2 = habitViewModel.insertWithId(Habit("Walking", "Sport", 10, 0, 20, 10))
            for (i in 0..4) {
                habitViewModel.insertDaySchedule(DaySchedule(i, id2))
            }
        }
    }

    private fun addHabitsForPrograming() {
        CoroutineScope(Dispatchers.IO).launch {
            val id = habitViewModel.insertWithId(Habit("Android", "Programing", 9, 15, 120, 50))
            for (i in 0..5) {
                habitViewModel.insertDaySchedule(DaySchedule(i, id))
            }
            val id2 = habitViewModel.insertWithId(Habit("Algorithms", "Programing", 15, 0,45, 30))
            for (i in 0..4) {
                habitViewModel.insertDaySchedule(DaySchedule(i, id2))
            }
        }
    }
}
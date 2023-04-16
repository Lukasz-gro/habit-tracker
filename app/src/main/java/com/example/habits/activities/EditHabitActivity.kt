package com.example.habits.activities

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.habits.HabitApplication
import com.example.habits.HabitViewModel
import com.example.habits.HabitViewModelFactory
import com.example.habits.R
import com.example.habits.activities.HabitsScheduleActivity.Companion.observeOnce
import com.example.habits.data.DaySchedule
import com.example.habits.data.Habit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditHabitActivity : AppCompatActivity() {
    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory((application as HabitApplication).repository)
    }
    private val chosenDays = mutableListOf(false, false, false, false, false, false, false)
    private lateinit var etHabitName: EditText
    private lateinit var etCategory: EditText
    private lateinit var etTimeStart: EditText
    private lateinit var etScore: EditText
    private lateinit var etDuration: EditText

    private lateinit var btnMon: Button
    private lateinit var btnTue: Button
    private lateinit var btnWed: Button
    private lateinit var btnThr: Button
    private lateinit var btnFri: Button
    private lateinit var btnSat: Button
    private lateinit var btnSun: Button

    private lateinit var habit: Habit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.extras?.getLong("id") ?: Long.MAX_VALUE
        if (id == Long.MAX_VALUE) {
          finish()
        }
        setContentView(R.layout.edit_habit)

        habitViewModel.getHabitById(id).observeOnce(this) { selectedHabit ->
            initialSetup(selectedHabit)
            habit = selectedHabit
        }

        habitViewModel.getDaysOfHabit(id).observeOnce(this) { days ->
            println(days)
            setupButtonsWeekdaysListeners(days)
        }

        findViewById<Button>(R.id.btnEditAddHabit).setOnClickListener {
            onSubmit()
        }
    }

    private fun initialSetup(habit: Habit) {
        etHabitName = findViewById(R.id.etEditName)
        etCategory = findViewById(R.id.etEditCategory)
        etTimeStart = findViewById(R.id.etEditHabitStart)
        etScore = findViewById(R.id.etEditScore)
        etDuration = findViewById(R.id.etEditHabitDuration)

        etHabitName.setText(habit.habitName)
        etCategory.setText(habit.habitCategory)
        val minutes = if (habit.minuteStart<10) "0${habit.minuteStart}" else habit.minuteStart.toString()
        etTimeStart.setText("${habit.hourStart}:${minutes}")
        etScore.setText(habit.score.toString())
        etDuration.setText(habit.duration.toString())
    }

    private fun onSubmit() {
        val editTexts = listOf(etHabitName, etCategory, etTimeStart, etDuration, etScore)
        for (i in editTexts.indices) {
            if (TextUtils.isEmpty(editTexts[i].text)) {
                return
            }
        }
        val splintedText = etTimeStart.text.split(":")
        try {
            val hour = splintedText[0].toInt()
            val minute = splintedText[1].toInt()
            if (hour > 24 || hour < 0 || minute > 59 || minute < 0) {
                throw Exception()
            }
        } catch (e: Exception) {
            return
        }

        val updatedHabit = habit.copy(
            habitName = etHabitName.text.toString(),
            habitCategory = etCategory.text.toString(),
            duration = etDuration.text.toString().toInt(),
            hourStart = splintedText[0].toInt(),
            minuteStart = splintedText[1].toInt(),
            score = etScore.text.toString().toInt()
        )
        updatedHabit.id = habit.id
        //update habit info
        CoroutineScope(Dispatchers.IO).launch {
            habitViewModel.updateHabit(updatedHabit)
        }

        CoroutineScope(Dispatchers.IO).launch {
            // remove current habit from from all days
            habitViewModel.deleteFromDaySchedule(habit.id)

            //add current habit to newly selected days
            for (i in chosenDays.indices) {
                if(chosenDays[i]) {
                    habitViewModel.insertDaySchedule(DaySchedule(i, habit.id))
                }
            }
        }
        finish()
    }

    private fun chooseDay(btn: Button, id: Int) {
        if (!chosenDays[id]) {
            btn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_button_selected))
            chosenDays[id] = true
        } else {
            btn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_button))
            chosenDays[id] = false
        }
    }

    private fun setupButtonsWeekdaysListeners(days: List<Int>) {
        btnMon = findViewById(R.id.btnEditMonday)
        btnTue = findViewById(R.id.btnEditTuesday)
        btnWed = findViewById(R.id.btnEditWednesday)
        btnThr = findViewById(R.id.btnEditThursday)
        btnFri = findViewById(R.id.btnEditFriday)
        btnSat = findViewById(R.id.btnEditSaturday)
        btnSun = findViewById(R.id.btnEditSunday)

        val buttons = listOf(btnMon, btnTue, btnWed, btnThr, btnFri, btnSat, btnSun)

        for (i in 0..6) {
            if (i in days) {
                buttons[i].backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_button_selected))
                chosenDays[i] = true
            }
            buttons[i].setOnClickListener {
                chooseDay(buttons[i], i)
            }
        }
    }
}
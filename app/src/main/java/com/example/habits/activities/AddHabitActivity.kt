package com.example.habits.activities

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.habits.R

class AddHabitActivity : AppCompatActivity(){
    private val chosenDays = mutableListOf(false, false, false, false, false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_habit)

        setupButtonsWeekdaysListeners()
        val button = findViewById<Button>(R.id.btnAddHabit)

        button.setOnClickListener {
            val replyIntent = Intent()
            if (getTextFromInputs(replyIntent)) {
                val temp = Array(7) { i -> if (chosenDays[i]) "1" else "0" }
                replyIntent.putExtra(DAYS, temp)
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }
    }

    private fun getTextFromInputs(replyIntent: Intent) : Boolean {
        val etHabitName = findViewById<EditText>(R.id.etName)
        val etCategory = findViewById<EditText>(R.id.etCategory)
        val etTimeStart = findViewById<EditText>(R.id.etHabitStart)
        val etScore = findViewById<EditText>(R.id.etScore)
        val etDuration = findViewById<EditText>(R.id.etHabitDuration)

        val editTexts = listOf(etHabitName, etCategory, etTimeStart, etDuration, etScore)
        val names = listOf(HABIT_NAME, HABIT_CATEGORY, TIME_START, HABIT_DURATION, SCORE)

        for (i in editTexts.indices) {
            if (TextUtils.isEmpty(editTexts[i].text)) {
               return false
            }
            replyIntent.putExtra(names[i], editTexts[i].text.toString())
        }
        val splintedText = etTimeStart.text.split(":")
        try {
            val hour = splintedText[0].toInt()
            val minute = splintedText[1].toInt()
            if (hour > 24 || hour < 0 || minute > 59 || minute < 0) {
                throw Exception()
            }
        } catch (e: Exception) {
            return false
        }
        return true
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

    private fun setupButtonsWeekdaysListeners() {
        val btnMon = findViewById<Button>(R.id.btnMonday)
        val btnTue = findViewById<Button>(R.id.btnTuesday)
        val btnWed = findViewById<Button>(R.id.btnWednesday)
        val btnThr = findViewById<Button>(R.id.btnThursday)
        val btnFri = findViewById<Button>(R.id.btnFriday)
        val btnSat = findViewById<Button>(R.id.btnSaturday)
        val btnSun = findViewById<Button>(R.id.btnSunday)

        val buttons = listOf(btnMon, btnTue, btnWed, btnThr, btnFri, btnSat, btnSun)

        for (i in 0..6) {
            buttons[i].setOnClickListener {
                chooseDay(buttons[i], i)
            }
        }
    }

    companion object {
        const val HABIT_NAME = "habitName"
        const val HABIT_CATEGORY = "category"
        const val SCORE = "score"
        const val TIME_START = "timeStart"
        const val HABIT_DURATION = "duration"
        const val DAYS = "days"
        const val MODE = "mode"
    }
}

package com.example.habits.activities

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.habits.HabitApplication
import com.example.habits.HabitViewModel
import com.example.habits.HabitViewModelFactory
import com.example.habits.R
import com.example.habits.activities.HabitsScheduleActivity.Companion.observeOnce
import java.time.LocalDate

class StatisticsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory((application as HabitApplication).repository)
    }

    private var day: Boolean = true
    private lateinit var currentDate: LocalDate
    private lateinit var scoreView: TextView
    private lateinit var completedView: TextView
    private lateinit var remainingView: TextView
    private lateinit var totalView: TextView
    private var category = "All categories"
    private var remaining = 0
    private var offset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        offset = intent.extras?.getInt("offset") ?: 0
        currentDate = LocalDate.now().plusDays(offset.toLong())
        setContentView(R.layout.statistics)

        scoreView = findViewById(R.id.tvScoreStats)
        completedView = findViewById(R.id.tvCompletedStats)
        remainingView = findViewById(R.id.tvPlannedStats)
        totalView = findViewById(R.id.tvTotalStats)

        val spinner = findViewById<Spinner>(R.id.spinnerCategories)
        val prevButton = findViewById<ImageView>(R.id.ivArrowLeft)
        val nextButton = findViewById<ImageView>(R.id.ivArrowRight)
        val dayButton = findViewById<Button>(R.id.btnDay)
        val weekButton = findViewById<Button>(R.id.btnWeek)

        findViewById<TextView>(R.id.tvDate).text = currentDate.toString()
        dayButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_button_selected))

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("All categories"))
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        habitViewModel.getCategoryList().observe(this) { categories ->
            categories.let {
                spinner.adapter = ArrayAdapter(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        extendedArrayOfCategories(categories))
            }
        }

        dayButton.setOnClickListener {
            day = true
            findViewById<TextView>(R.id.tvDate).text = "$currentDate"
            dayButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_button_selected))
            weekButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_button))
            updateStatsForDayCategory()
        }

        weekButton.setOnClickListener {
            day = false
            findViewById<TextView>(R.id.tvDate).text = "${currentDate.plusDays(-7)} to $currentDate"
            weekButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_button_selected))
            dayButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_button))
            updateStatsForWeekCategory()
        }

        prevButton.setOnClickListener {
            if (day) {
                currentDate = currentDate.plusDays(-1)
                findViewById<TextView>(R.id.tvDate).text = currentDate.toString()
                updateStatsForDayCategory()
            } else {
                currentDate = currentDate.plusDays(-7)
                findViewById<TextView>(R.id.tvDate).text = "${currentDate.plusDays(-7)} to $currentDate"
                updateStatsForWeekCategory()
            }
        }

        nextButton.setOnClickListener {
            if (day) {
                currentDate = currentDate.plusDays(1)
                findViewById<TextView>(R.id.tvDate).text = currentDate.toString()
                updateStatsForDayCategory()
            } else {
                currentDate = currentDate.plusDays(7)
                findViewById<TextView>(R.id.tvDate).text = "${currentDate.plusDays(-7)} to $currentDate"
                updateStatsForWeekCategory()
            }
        }
    }

    private fun updateStatsForDayCategory() {
        habitViewModel.getScoreForDay(currentDate.dayOfYear, category).observeOnce(this) { score ->
            setScoreView(score)
        }

        habitViewModel.getRemainingHabits(0, currentDate.dayOfYear, category).observeOnce(this) { count ->
            setRemainingView(count)
        }

        habitViewModel.getHabitsCountForDay(0, category).observeOnce(this) { count ->
            remaining = count
            setTotalCount(count)
        }

        habitViewModel.countCompletedInDay(currentDate.dayOfYear, category).observeOnce(this) { count ->
            setCompleteView(count)
        }
    }

    private fun updateStatsForWeekCategory() {
        habitViewModel.countCompletedInDay(currentDate.dayOfYear, category).observeOnce(this) { count ->
            setCompleteView(count)
        }

        habitViewModel.getScoreForWeek(currentDate.dayOfYear, category).observeOnce(this) { score ->
            setScoreView(score)
        }

        habitViewModel.getHabitsCountForWeek(category).observeOnce(this) { count ->
            setTotalCount(count)
        }

        habitViewModel.countCompletedInWeek(currentDate.dayOfYear, category).observeOnce(this) { count ->
            setCompleteView(count)
        }

        habitViewModel.getRemainingHabitsForWeek(0, currentDate.dayOfYear, category).observeOnce(this) { count ->
            setRemainingView(count - remaining)
        }
    }

    private fun extendedArrayOfCategories(categories: List<String>): List<String> {
        return listOf("All categories") + categories
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, p3: Long) {
        category = parent.getItemAtPosition(position).toString()
        if (day) {
            updateStatsForDayCategory()
        } else {
            updateStatsForWeekCategory()
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        return
    }

    private fun setScoreView(score: Int?) {
        scoreView.text = "Score: ${score ?: 0}"
    }

    private fun setCompleteView(count: Int) {
        if (day) {
            remaining = count
        }
        completedView.text = "Completed: $count"
    }

    private fun setTotalCount(count: Int) {
        totalView.text = "Total: $count"
    }

    private fun setRemainingView(count: Int) {
        remainingView.text = "To do: $count"
    }
}

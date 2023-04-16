package com.example.habits.activities

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.habits.HabitApplication
import com.example.habits.HabitViewModel
import com.example.habits.HabitViewModelFactory
import com.example.habits.R
import java.time.LocalDate

class StatisticsActivity : AppCompatActivity() {
    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory((application as HabitApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val offset = intent.extras?.getInt("offset") ?: 0
        val currentDate = LocalDate.now().plusDays(offset.toLong())
        setContentView(R.layout.statistics)

        val scoreView = findViewById<TextView>(R.id.tvScoreStats)
        val completedView = findViewById<TextView>(R.id.tvCompletedStats)
        val remainingView = findViewById<TextView>(R.id.tvPlannedStats)
        val totalView = findViewById<TextView>(R.id.tvTotalStats)
        val categoryView = findViewById<TextView>(R.id.tvCategoryToday)

        findViewById<TextView>(R.id.tvDate).text = currentDate.toString()

        habitViewModel.getScoreForDay(currentDate.dayOfYear).observe(this) { score ->
            score.let { scoreView.text = "Score: ${score ?: 0}" }
        }

        habitViewModel.getRemainingHabits(0, currentDate.dayOfYear).observe(this) { count ->
            count.let { remainingView.text = "To do: $count" }
        }

        habitViewModel.getHabitsCountForDay(0).observe(this) { count ->
            count.let { totalView.text = "Total: $count" }
        }

        habitViewModel.countCompletedInDay(currentDate.dayOfYear).observe(this) { count ->
            count.let { completedView.text = "Completed: $count" }
        }

        habitViewModel.getMostPopularCategory(0).observe(this) { category ->
            category.let { categoryView.text = "Your category for today is: $category" }
        }
    }
}

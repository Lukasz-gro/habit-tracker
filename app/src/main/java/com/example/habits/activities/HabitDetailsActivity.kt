package com.example.habits.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.habits.HabitApplication
import com.example.habits.HabitViewModel
import com.example.habits.HabitViewModelFactory
import com.example.habits.R
import com.example.habits.activities.HabitsScheduleActivity.Companion.observeOnce
import com.example.habits.data.Habit
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitDetailsActivity : AppCompatActivity() {
    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory((application as HabitApplication).repository)
    }
    lateinit var habit: Habit
    var id: Long = 0
    private lateinit var habitPicture: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.extras?.getLong("id") ?: 0
        habitViewModel.getHabitById(id).observe(this) { selectedHabit ->
            if (selectedHabit != null) {
                habit = selectedHabit
                bind(habit)
            }
        }

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.habit_details)

        habitPicture = findViewById(R.id.ivHabitPicture)

        habitViewModel.getPicturePath(id, 0, LocalDate.now().dayOfYear).observeOnce(this) {
            updateImage(it, habitPicture)
        }

        findViewById<ImageView>(R.id.ivDelete).setOnClickListener {
           this.lifecycleScope.launch {
               habitViewModel.deleteHabitById(id)
           }
            finish()
        }

        findViewById<ImageView>(R.id.ivCamera).setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("id", id)

            this.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        habitViewModel.getPicturePath(id, 0, LocalDate.now().dayOfYear).observeOnce(this) {
            updateImage(it, habitPicture)
        }
    }
    private fun updateImage(path: String?, habitPicture: ImageView) {
        Glide.with(this)
            .load(path)
            .placeholder(R.drawable.baseline_xd)
            .into(habitPicture)
    }

    @SuppressLint("SetTextI18n")
    private fun bind(habit: Habit) {
        val habitName = findViewById<TextView>(R.id.tvHabitName)
        habitName.text = habit.habitName
        val row1 = findViewById<TextView>(R.id.tvRow1)
        row1.text = habit.habitCategory
        val row2 = findViewById<TextView>(R.id.tvRow2)
        val minutes = if (habit.minuteStart<10) "0${habit.minuteStart}" else habit.minuteStart.toString()
        row2.text = "${habit.hourStart}:${minutes}"
        val row3 = findViewById<TextView>(R.id.tvRow3)
        row3.text = habit.duration.toString()
        val row4 = findViewById<TextView>(R.id.tvRow4)
        row4.text = habit.score.toString()
    }
}
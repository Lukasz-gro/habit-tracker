package com.example.habits.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.HabitApplication
import com.example.habits.HabitViewModel
import com.example.habits.HabitViewModelFactory
import com.example.habits.R
import com.example.habits.activities.AddHabitActivity.Companion.DAYS
import com.example.habits.activities.AddHabitActivity.Companion.HABIT_CATEGORY
import com.example.habits.activities.AddHabitActivity.Companion.HABIT_DURATION
import com.example.habits.activities.AddHabitActivity.Companion.HABIT_NAME
import com.example.habits.activities.AddHabitActivity.Companion.SCORE
import com.example.habits.activities.AddHabitActivity.Companion.TIME_START
import com.example.habits.adapter.HabitItemAdapter
import com.example.habits.alarms.NotificationAlarmScheduler
import com.example.habits.data.DayCompletion
import com.example.habits.data.DaySchedule
import com.example.habits.data.Habit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitsScheduleActivity : AppCompatActivity() {
     private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory((application as HabitApplication).repository)
    }
    private lateinit var scheduler: NotificationAlarmScheduler
    private var useNotifications: Boolean = true
    private val newHabitActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.habit_schedule)

        scheduler = NotificationAlarmScheduler(this)
        val recyclerView = findViewById<RecyclerView>(R.id.rvHabits)

        val adapter = HabitItemAdapter(
            {
                val intent = Intent(this, HabitDetailsActivity::class.java)
                intent.putExtra("id", it.id)
                this.startActivity(intent)
            },
            {
                val intent = Intent(this, EditHabitActivity::class.java)
                intent.putExtra("id", it.id)
                this.startActivity(intent)
            },
            { habit, _ ->
                run {
                    CoroutineScope(Dispatchers.IO).launch {
                        val doy = LocalDate.now().dayOfYear

                        val dayCompletion = habitViewModel.getDayCompletion(doy, habit.id)
                        if (dayCompletion == null) {
                            habitViewModel.insertHabitWithStatus(DayCompletion(doy, habit.id,true))
                        } else if (dayCompletion.done){
                            val temp = dayCompletion.copy(done = false)
                            temp.id = dayCompletion.id
                            habitViewModel.updateDayCompletion(temp)
                        } else {
                            val temp = dayCompletion.copy(done = true)
                            temp.id = dayCompletion.id
                            habitViewModel.updateDayCompletion(temp)
                        }
                    }
                }
            }, { habit, checkBox ->
                    run {
                        val doy = LocalDate.now().dayOfYear
                        habitViewModel.getStatusOfHabit(doy, habit.id).observeOnce(this) { res ->
                            if (res != null) {
                                checkBox.isChecked = res
                            }
                        }
                    }
            })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val countPrompt = findViewById<TextView>(R.id.tvHabitCountPrompt)
        val addButton = findViewById<Button>(R.id.materialButton)
        val scoreView = findViewById<TextView>(R.id.tvTodayScore)
        val statsIcon = findViewById<ImageView>(R.id.ivStatistics)
        val notificationIcon = findViewById<ImageView>(R.id.ivNotifications)
        val calendarIcon = findViewById<ImageView>(R.id.ivCalendar)

        addButton.setOnClickListener {
            val intent = Intent(this, AddHabitActivity::class.java)
            startActivityForResult(intent, newHabitActivityRequestCode)
        }

        statsIcon.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            intent.putExtra("offset", 0)
            this.startActivity(intent)
        }

        notificationIcon.setOnClickListener {
            useNotifications = if(useNotifications) {
                Toast.makeText(this, "Notifications off", Toast.LENGTH_SHORT).show()
                notificationIcon.setImageResource(R.drawable.baseline_notifications_off_24)
                false
            } else {
                Toast.makeText(this, "Notifications on", Toast.LENGTH_SHORT).show()
                notificationIcon.setImageResource(R.drawable.baseline_notifications_24)
                true
            }
        }

        habitViewModel.getScoreForDay(LocalDate.now().dayOfYear).observe(this) { score ->
            score.let { scoreView.text = "Today score: ${score ?: 0}" }
        }
        habitViewModel.getHabitsForDay(0).observe(this) { habits ->
            habits.let { adapter.submitList(it) }
        }

        habitViewModel.getRemainingHabits(0, LocalDate.now().dayOfYear).observe(this) { count ->
            count.let { countPrompt.text = "You have $it habit left for today" }
        }

//        calendarIcon.setOnClickListener {
//            val intent = Intent(this, CameraActivity::class.java)
//            this.startActivity(intent)
//        }
    }

    override fun onStop() {
        super.onStop()
        if (useNotifications) {
            CoroutineScope(Dispatchers.IO).launch {
                val doy = LocalDate.now().dayOfYear

                val toDo = habitViewModel.getRemainingHabitsUntilHour(0, doy)
                val hourEnds = toDo.map {
                    (it.hourStart + (it.minuteStart + it.duration)/60) * 100 + (it.minuteStart + it.duration)%60
                } as ArrayList

                scheduler.schedule(hourEnds)
            }
        } else {
            scheduler.cancel()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newHabitActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val name = data?.getStringExtra(HABIT_NAME)
            val category = data?.getStringExtra(HABIT_CATEGORY)
            val score = data?.getStringExtra(SCORE)?.toInt()
            val days = data?.getStringArrayExtra(DAYS) ?: Array(7) { "0" }
            val timeStart = data?.getStringExtra(TIME_START)
            val duration = data?.getStringExtra(HABIT_DURATION)

            if (name != null && category != null && score != null && timeStart != null && duration != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val id = habitViewModel.insertWithId(createHabit(name, category, score, timeStart, duration.toInt()))
                    for (i in days.indices) {
                        if (days[i] == "1") {
                            habitViewModel.insertDaySchedule(DaySchedule(i, id))
                        }
                    }
                }
            }
        }
    }

    private fun createHabit(habitName: String, category: String, score: Int, timeStart: String, duration: Int) : Habit {
        val splinted = timeStart.split(":")
        return Habit(habitName, category, splinted[0].toInt(), splinted[1].toInt(), duration, score)
    }

    companion object {
        fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
            observe(lifecycleOwner, object : Observer<T> {

                override fun onChanged(value: T) {
                    observer.onChanged(value)
                    removeObserver(this)
                }
            })
        }
    }

}

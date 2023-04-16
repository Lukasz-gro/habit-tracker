package com.example.habits.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.R
import com.example.habits.data.Habit

class HabitItemAdapter(
    private val clickListener: (Habit) -> Unit,
    private val editListener: (Habit) -> Unit,
    private val checkBoxListener: (Habit, CheckBox) -> Unit,
    private val setCheckBox: (Habit, CheckBox) -> Unit
) : ListAdapter<Habit, HabitItemAdapter.HabitViewHolder>(HabitComparator()) {

    class HabitViewHolder(
        private val view: View,
        private val clickAtPosition: (Int) -> Unit,
        private val clickAtEdit: (Int) -> Unit,
        private val checkBoxClicked: (Int, CheckBox) -> Unit,
    ) : RecyclerView.ViewHolder(view) {
        private val habitName: TextView = view.findViewById(R.id.tvHabitName)
        private val habitTime: TextView = view.findViewById(R.id.tvTime)
        private val habitCategory: TextView = view.findViewById(R.id.tvHabitCategory)
        private val editButton: ImageView = view.findViewById(R.id.ivEditIcon)
        val cbHabitsDone: CheckBox = view.findViewById(R.id.cbHabitDone)

        fun bindName(text: String?) {
            habitName.text = text
        }

        fun bindTime(hourStart: Int, minuteStart: Int, duration: Int) {
            val hourEnd = (hourStart + (duration + minuteStart) / 60) % 24
            val minuteEnd = (minuteStart + duration) % 60
            val minute = if (minuteStart < 10) "0${minuteStart}" else minuteStart.toString()
            val minuteEndString = if (minuteEnd < 10) "0${minuteEnd}" else minuteEnd.toString()
            val time = "$hourStart:$minute ~ $hourEnd:$minuteEndString"
            habitTime.text = time
        }

        fun bindCategory(text: String?) {
            val newText = "- $text"
            habitCategory.text = newText
        }

        init {
            cbHabitsDone.setOnClickListener {
                checkBoxClicked(adapterPosition, cbHabitsDone)
            }

            view.setOnClickListener {
                clickAtPosition(adapterPosition)
            }

            editButton.setOnClickListener {
                clickAtEdit(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.habit_item, parent, false)
        return HabitViewHolder(view,
            { clickListener(getItem(it)) },
            { editListener(getItem(it)) },
            { a, b ->  checkBoxListener(getItem(a), b) })
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val item = getItem(position)
        setCheckBox(item, holder.cbHabitsDone)
        holder.bindName(item.habitName)
        holder.bindCategory(item.habitCategory)
        holder.bindTime(item.hourStart, item.minuteStart, item.duration)
    }
}

class HabitComparator : DiffUtil.ItemCallback<Habit>() {
    override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem.habitName === newItem.habitName
    }

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem.habitName == newItem.habitName
    }
}

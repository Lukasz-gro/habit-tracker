package com.example.habits.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.HabitApplication
import com.example.habits.HabitViewModel
import com.example.habits.HabitViewModelFactory
import com.example.habits.R
import com.example.habits.adapter.PhotoItemAdapter

class GalleryActivity : AppCompatActivity() {
    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory((application as HabitApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.gallery_view)
        val recyclerView = findViewById<RecyclerView>(R.id.rvGallery)
        val adapter = PhotoItemAdapter(this) {
            val intent = Intent(this, PairedDevicesActivity::class.java)
            intent.putExtra("photoToSend", it)
            this.startActivity(intent)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        habitViewModel.getAllPictures().observe(this) {photos ->
            photos.let { adapter.submitList(it) }
        }
    }
}
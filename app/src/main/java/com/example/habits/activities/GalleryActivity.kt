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
//            val photo = File(
//                this.getExternalFilesDir(Environment.DIRECTORY_DCIM)!!.absolutePath,
//                LocalDate.now().toString() + Random.nextInt(0, 10000)
//            )
//            if (photo.exists()) {
//                photo.delete()
//            }
//            try {
//                val fos = FileOutputStream(photo.path)
//                fos.write(it)
//                fos.close()

//                CoroutineScope(Dispatchers.IO).launch {
//                    println("Pisanie do pliku skonczone")
//
//                    val newPhoto = com.example.habits.data.Photo(
//                        description = "No description",
//                        path = photo.path,
//                        habitId = -1,
//                        day = 0,
//                        doy = LocalDate.now().dayOfYear
//                    )
//                    habitViewModel.insertPhoto(newPhoto)
//                }

//            } catch (e: Exception) {
//                println(e)
//                println("Error przy pisaniu do pliku")
//            }
//            intent.putExtra("photoPath", it)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        habitViewModel.getAllPictures().observe(this) {photos ->
            photos.let { adapter.submitList(it) }
        }
    }
}
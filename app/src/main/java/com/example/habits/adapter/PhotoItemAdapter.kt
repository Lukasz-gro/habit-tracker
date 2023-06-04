package com.example.habits.adapter

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.habits.R

import com.example.habits.data.Photo
import java.io.ByteArrayOutputStream
import java.io.File

class PhotoItemAdapter(
    private val context: Context,
    private val sharePictureOnClick: (image: ByteArray) -> Unit
) : ListAdapter<Photo, PhotoItemAdapter.PhotoViewHolder>(PhotoComparator()) {

    class PhotoViewHolder(
        private val view: View,
        private val context: Context,
        private val sharePictureOnClick: (photoInBytes: ByteArray) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val picture: ImageView = view.findViewById(R.id.ivHabitPicture)

        fun bindPicture(path: String) {
            Glide.with(context)
                .load(path)
                .placeholder(R.drawable.baseline_xd)
                .into(picture)
        }

        init {
            picture.setOnClickListener {
                val bitmap = picture.drawable.toBitmap()
                val os = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                val imageInBytes = os.toByteArray()
                sharePictureOnClick(imageInBytes)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return PhotoViewHolder(view, context) { sharePictureOnClick(it) }
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindPicture(item.path)
    }
}

class PhotoComparator : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldPhoto: Photo, newPhoto: Photo): Boolean {
        return oldPhoto.path == newPhoto.path
    }

    override fun areContentsTheSame(oldPhoto: Photo, newPhoto: Photo): Boolean {
        return oldPhoto.path == newPhoto.path
    }
}
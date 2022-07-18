package com.example.unsplashapp.ui.favoritescreen.list

import androidx.recyclerview.widget.DiffUtil
import com.example.unsplashapp.data.models.Photo

object FavoriteDiffUtil : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }

}
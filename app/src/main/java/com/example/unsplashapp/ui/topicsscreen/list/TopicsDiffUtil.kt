package com.example.unsplashapp.ui.topicsscreen.list

import androidx.recyclerview.widget.DiffUtil
import com.example.unsplashapp.data.models.Topic

object TopicsDiffUtil : DiffUtil.ItemCallback<Topic>() {
    override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean =
        oldItem == newItem


    override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.title == newItem.title &&
                oldItem.coverPhoto == newItem.coverPhoto &&
                oldItem.totalPhotos == newItem.totalPhotos
    }
}


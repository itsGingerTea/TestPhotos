package com.example.unsplashapp.ui.photolistscreen

import com.example.unsplashapp.data.models.Photo

interface PhotosCallback {
    fun onItemClick(photo: Photo)
    fun onLikeClick(like: Boolean, photo: Photo)
}
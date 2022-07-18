package com.example.unsplashapp.data.models

import com.google.gson.annotations.SerializedName

data class Topic(
    val id: String,
    @SerializedName("total_photos") val totalPhotos: Int,
    val title: String,
    @SerializedName("cover_photo") val coverPhoto: CoverPhoto,
)

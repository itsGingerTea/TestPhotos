package com.example.unsplashapp.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "favoritePhoto")
data class Photo(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "desc")
    val description: String? = null,

    @ColumnInfo(name = "likes")
    val likes: Int? = null,

    @ColumnInfo(name = "favorite")
    @SerializedName("liked_by_user")
    var favorite: Boolean,

    @Embedded
    val urls: Urls,

    @Embedded
    val user: User,
) : Parcelable
package com.example.unsplashapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.unsplashapp.data.models.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert
    suspend fun addFavoritesToPhotoCache(photoList: List<Photo>?)

    @Query("DELETE FROM favoritePhoto")
    suspend fun deleteAllFavoritePhoto()

    @Transaction
    suspend fun clearAndAdd(photoList: List<Photo>?) {
        deleteAllFavoritePhoto()
        addFavoritesToPhotoCache(photoList)
    }

    @Query("DELETE FROM favoritePhoto WHERE id = :id")
    suspend fun deleteFromFavoriteCache(id: String)

    @Query("UPDATE favoritePhoto SET favorite = :favorite WHERE id= :id")
    suspend fun updatePhotoFromFavoriteCache(id: String, favorite: Boolean)

    @Query("SELECT * FROM favoritePhoto")
    fun getAllFavoritePhoto(): Flow<List<Photo>>
}
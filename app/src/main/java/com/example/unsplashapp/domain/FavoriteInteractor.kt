package com.example.unsplashapp.domain

import com.example.unsplashapp.utils.ApiState
import com.example.unsplashapp.utils.ViewState
import com.example.unsplashapp.data.repository.PhotoRepository
import com.example.unsplashapp.data.repository.PrefsRepository
import com.example.unsplashapp.data.models.Photo
import com.example.unsplashapp.data.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteInteractor @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val prefsRepository: PrefsRepository
) {
    val updatedFavorites: Flow<List<Photo>> = photoRepository.getAllPhotoFromFavoriteCache()

    suspend fun getLikesAndAddToCache(username: String): ApiState<*> {
        return photoRepository.getLikes(username)
    }

    suspend fun updatePhoto(idPhoto: String, fav: Boolean): ViewState {
        return photoRepository.setLike(idPhoto, fav)
    }

    fun getUserInfo(): User {
        return prefsRepository.getUserInfo()
    }
}
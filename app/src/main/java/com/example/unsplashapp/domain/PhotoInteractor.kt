package com.example.unsplashapp.domain

import android.content.Context
import androidx.paging.PagingSource
import com.example.unsplashapp.data.models.Photo
import com.example.unsplashapp.data.repository.PhotoRepository
import com.example.unsplashapp.data.repository.PrefsRepository
import com.example.unsplashapp.utils.ViewState
import javax.inject.Inject

class PhotoInteractor @Inject constructor(
    private val photoRepo: PhotoRepository,
    private val prefsRepo: PrefsRepository
) {

    fun getPagingSourceFactory(query: String): PagingSource<Int, Photo> {
        return photoRepo.getPhotosPagingSourceFactory(query)
    }

    suspend fun updatePhoto(idPhoto: String, fav: Boolean): ViewState {
        return photoRepo.setLike(idPhoto, fav)
    }

    fun isLogged(): Boolean {
        return prefsRepo.isLogged()
    }

    suspend fun downloadPhoto(flagSdk: Boolean, url: String, context: Context): ViewState {
        return photoRepo.downloadPhoto(flagSdk, url, context)
    }
}
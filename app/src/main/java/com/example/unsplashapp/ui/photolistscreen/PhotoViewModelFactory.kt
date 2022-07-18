package com.example.unsplashapp.ui.photolistscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.unsplashapp.di.mainscreen.MainScope
import com.example.unsplashapp.domain.AuthInteractor
import com.example.unsplashapp.domain.PhotoInteractor
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class PhotoViewModelFactory @AssistedInject constructor(
    private val authInteractor: AuthInteractor,
    private val photoInteractor: PhotoInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == PhotoViewModel::class.java)
        return PhotoViewModel(authInteractor, photoInteractor) as T
    }

    @AssistedFactory
    @MainScope
    interface Factory {
        fun create(): PhotoViewModelFactory
    }
}
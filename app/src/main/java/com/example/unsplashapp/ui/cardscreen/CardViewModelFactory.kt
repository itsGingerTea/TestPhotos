package com.example.unsplashapp.ui.cardscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.unsplashapp.di.mainscreen.MainScope
import com.example.unsplashapp.domain.PhotoInteractor
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CardViewModelFactory @AssistedInject constructor(
    private val photoInteractor: PhotoInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == CardViewModel::class.java)
        return CardViewModel(photoInteractor) as T
    }

    @AssistedFactory
    @MainScope
    interface Factory {
        fun create(): CardViewModelFactory
    }
}
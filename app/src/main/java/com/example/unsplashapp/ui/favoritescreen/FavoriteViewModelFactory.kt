package com.example.unsplashapp.ui.favoritescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.unsplashapp.di.mainscreen.MainScope
import com.example.unsplashapp.domain.FavoriteInteractor
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class FavoriteViewModelFactory @AssistedInject constructor(
    private val favInteractor: FavoriteInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == FavoriteViewModel::class.java)
        return FavoriteViewModel(favInteractor) as T
    }

    @AssistedFactory
    @MainScope
    interface Factory {
        fun create(): FavoriteViewModelFactory
    }
}
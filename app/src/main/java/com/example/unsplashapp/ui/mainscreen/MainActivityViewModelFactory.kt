package com.example.unsplashapp.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.unsplashapp.di.mainscreen.MainScope
import com.example.unsplashapp.domain.AuthInteractor
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class MainActivityViewModelFactory @AssistedInject constructor(
    private val authInteractor: AuthInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == MainActivityViewModel::class.java)
        return MainActivityViewModel(authInteractor) as T
    }

    @AssistedFactory
    @MainScope
    interface Factory {
        fun create(): MainActivityViewModelFactory
    }
}
package com.example.unsplashapp.ui.topicsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.unsplashapp.di.mainscreen.MainScope
import com.example.unsplashapp.domain.TopicsInteractor
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class TopicsViewModelFactory @AssistedInject constructor(
    private val topicsInteractor: TopicsInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == TopicsViewModel::class.java)
        return TopicsViewModel(topicsInteractor) as T
    }

    @AssistedFactory
    @MainScope
    interface Factory {
        fun create(): TopicsViewModelFactory
    }
}
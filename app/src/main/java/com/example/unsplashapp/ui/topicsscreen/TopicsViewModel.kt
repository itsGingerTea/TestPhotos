package com.example.unsplashapp.ui.topicsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.unsplashapp.data.models.Topic
import com.example.unsplashapp.domain.TopicsInteractor
import com.example.unsplashapp.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopicsViewModel @Inject constructor(
    private val topicsInteractor: TopicsInteractor
) : ViewModel() {

    val state: SingleLiveEvent<ViewState> = SingleLiveEvent()

    val topics: Flow<PagingData<Topic>> = Pager(
        config = PagingConfig(pageSize = 5, enablePlaceholders = false)
    ) {
        topicsInteractor.getPagingSourceFactory()
    }.flow
        .cachedIn(viewModelScope)

    fun onCreate() {
        val isLogged = topicsInteractor.isLogged()
        if (isLogged) {
            getUserInfo()
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            when (topicsInteractor.getUserInfo()) {
                is Success<*> -> state.postValue(SuccessState)
                Error -> state.postValue(ErrorState)
                else -> {}
            }
        }
    }

    fun onLogoutClicked() {
        topicsInteractor.logout()
    }
}
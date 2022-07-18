package com.example.unsplashapp.ui.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unsplashapp.domain.AuthInteractor
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : ViewModel() {

    private val _loginResult: MutableLiveData<Boolean> = MutableLiveData()
    val loginResult: LiveData<Boolean> get() = _loginResult

    fun onCreate() {
        _loginResult.postValue(authInteractor.isLogged())
    }
}
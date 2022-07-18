package com.example.unsplashapp.ui.authscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplashapp.utils.SingleLiveEvent
import com.example.unsplashapp.domain.AuthInteractor
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthUnsplashViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : ViewModel() {

    val tokenResult = SingleLiveEvent<Unit>()

    fun onAuthCodeExtracted(code: String) {
        authInteractor.saveAuthCode(code)
        loadToken(code)
    }

    private fun loadToken(code: String) {
        viewModelScope.launch {
            val token = authInteractor.getTokenFromNetwork(code)
            token.accessToken.let { authInteractor.saveToken(it) }
            tokenResult.call()
        }
    }
}
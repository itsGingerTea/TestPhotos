package com.example.unsplashapp.domain

import com.example.unsplashapp.data.repository.PrefsRepository
import com.example.unsplashapp.data.repository.TokenRepository
import com.example.unsplashapp.data.models.AuthToken
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val prefsRepo: PrefsRepository,
    private val tokenRepo: TokenRepository
) {

    fun saveToken(token: String) {
        prefsRepo.saveToken(token)
    }

    fun isLogged(): Boolean {
        return prefsRepo.isLogged()
    }

    fun saveAuthCode(code: String) {
        prefsRepo.saveAuthCode(code)
    }

    suspend fun getTokenFromNetwork(code: String) : AuthToken {
      return tokenRepo.getToken(code)
    }
}
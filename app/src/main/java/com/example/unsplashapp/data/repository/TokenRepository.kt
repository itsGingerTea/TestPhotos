package com.example.unsplashapp.data.repository

import com.example.unsplashapp.data.models.AuthToken

interface TokenRepository {
    suspend fun getToken(code: String): AuthToken
}
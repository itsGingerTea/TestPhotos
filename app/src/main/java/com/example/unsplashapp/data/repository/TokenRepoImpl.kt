package com.example.unsplashapp.data.repository

import com.example.unsplashapp.data.models.AuthToken
import com.example.unsplashapp.data.network.ApiPhoto
import javax.inject.Inject
import javax.inject.Named

class TokenRepoImpl @Inject constructor(@Named("auth_api") private val api: ApiPhoto) :
    TokenRepository {
    override suspend fun getToken(code: String): AuthToken {
        return api.getToken(code)
    }
}
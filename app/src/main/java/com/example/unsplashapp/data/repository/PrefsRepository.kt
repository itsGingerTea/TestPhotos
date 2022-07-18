package com.example.unsplashapp.data.repository

import com.example.unsplashapp.data.models.User

interface PrefsRepository {
    fun saveToken(token: String)
    fun clear()
    fun isLogged(): Boolean
    fun getToken(): String?
    fun getAuthCode(): String?
    fun saveAuthCode(code: String)
    fun saveUserInfo(user: User)
    fun getUserInfo(): User
}
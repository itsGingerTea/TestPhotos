package com.example.unsplashapp.data.repository

import android.content.SharedPreferences
import com.example.unsplashapp.data.models.User
import com.google.gson.Gson
import javax.inject.Inject

class PrefsRepoImpl @Inject constructor(private val sharedPref: SharedPreferences) :
    PrefsRepository {

    override fun saveToken(token: String) {
        sharedPref.edit().putString(TOKEN_PREFS, token).apply()
    }

    override fun clear() {
        sharedPref.edit().clear().apply()
    }

    override fun isLogged(): Boolean {
        return sharedPref.contains(TOKEN_PREFS)
    }

    override fun saveUserInfo(user: User) {
        sharedPref.edit()
            .putString(USER, Gson().toJson(user).toString())
            .apply()
    }

    override fun getUserInfo(): User {
        return Gson().fromJson(sharedPref.getString(USER, ""), User::class.java)
    }


    override fun getToken(): String? {
        return sharedPref.getString(TOKEN_PREFS, null)
    }

    override fun getAuthCode(): String? {
        return sharedPref.getString(AUTH_CODE, null)
    }

    override fun saveAuthCode(code: String) {
        sharedPref.edit().putString(AUTH_CODE, code).apply()
    }

    companion object {
        const val TOKEN_PREFS = "access_token"
        const val AUTH_CODE = "auth_code"
        const val USER = "USER"
    }
}
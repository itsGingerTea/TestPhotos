package com.example.unsplashapp.data.network

import android.util.Log
import com.example.unsplashapp.BuildConfig
import com.example.unsplashapp.data.repository.PrefsRepository
import okhttp3.Interceptor
import okhttp3.Response

class LoggedInterceptor(private val prefsRepo: PrefsRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain
            .request()
        val url = original.url.newBuilder()
            .addQueryParameter("client_id", BuildConfig.CLIENT_ID)
            .build()
        val newRequest = original.newBuilder()
            .url(url)
            .build()
        if (prefsRepo.getToken() != null) {
            val authRequest = original.newBuilder()
                .url(url)
                .addHeader(AUTHORIZATION_KEY, String.format("Bearer %s", prefsRepo.getToken()))
                .build()
            Log.d("tokens", prefsRepo.getToken().toString())
            return chain.proceed(authRequest)
        }
        return chain.proceed(newRequest)
    }

    companion object {
        const val AUTHORIZATION_KEY = "Authorization"
    }
}
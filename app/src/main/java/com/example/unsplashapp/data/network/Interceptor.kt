package com.example.unsplashapp.data.network

import com.example.unsplashapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class Interceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain
            .request()
        val url = original.url.newBuilder()
            .addQueryParameter("client_id", BuildConfig.CLIENT_ID)
            .addQueryParameter("client_secret", BuildConfig.CLIENT_SECRET)
            .addQueryParameter("redirect_uri", BuildConfig.REDIRECT_URI)
            .addQueryParameter("grant_type", BuildConfig.GRANT_TYPE)
            .build()
        val request = original.newBuilder()
            .url(url)
            .build()
        return chain.proceed(request)
    }
}
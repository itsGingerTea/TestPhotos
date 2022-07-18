package com.example.unsplashapp.data.network

import com.example.unsplashapp.data.models.AuthToken
import com.example.unsplashapp.data.models.Photo
import com.example.unsplashapp.data.models.Topic
import com.example.unsplashapp.data.models.User
import retrofit2.Response
import retrofit2.http.*

interface ApiPhoto {
    @POST("/oauth/token")
    suspend fun getToken(
        @Query("code") code: String
    ): AuthToken

    @GET("topics/{id_or_slug}/photos")
    suspend fun getPhotos(
        @Path("id_or_slug") id: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<Photo>>

    @GET("topics")
    suspend fun getTopics(
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
    ): Response<List<Topic>>

    @GET("users/{username}/likes")
    suspend fun getLikes(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<Photo>>

    @GET("me")
    suspend fun getUserInfo(): Response<User>

    @POST("photos/{id}/like")
    suspend fun likeAPhoto(
        @Path("id") idPhoto: String
    )

    @DELETE("photos/{id}/like")
    suspend fun unlikeAPhoto(
        @Path("id") idPhoto: String
    )
}
package com.example.unsplashapp.data.repository

import android.content.Context
import androidx.paging.PagingSource
import com.example.unsplashapp.data.models.Photo
import com.example.unsplashapp.data.models.Topic
import com.example.unsplashapp.utils.ApiState
import com.example.unsplashapp.utils.ViewState
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface PhotoRepository {
    suspend fun getPhotos(topicId: String, page: Int, pageSize: Int): Response<List<Photo>>

    suspend fun setLike(idPhoto: String, like: Boolean): ViewState

    suspend fun getLikes(username: String): ApiState<*>

    suspend fun getUserInfo(): ApiState<*>

    suspend fun downloadPhoto(flagSdk: Boolean, url: String, context: Context): ViewState

    fun getTopicsPagingSourceFactory(): PagingSource<Int, Topic>

    fun getPhotosPagingSourceFactory(query: String): PagingSource<Int, Photo>

    fun getAllPhotoFromFavoriteCache(): Flow<List<Photo>>
}
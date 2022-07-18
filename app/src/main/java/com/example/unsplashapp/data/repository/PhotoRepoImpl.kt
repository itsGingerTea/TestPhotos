package com.example.unsplashapp.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import com.example.unsplashapp.data.MediaManager
import com.example.unsplashapp.data.PhotosPagingSource
import com.example.unsplashapp.data.TopicsPagingSource
import com.example.unsplashapp.data.models.Photo
import com.example.unsplashapp.data.models.Topic
import com.example.unsplashapp.data.models.User
import com.example.unsplashapp.data.network.ApiPhoto
import com.example.unsplashapp.data.room.PhotoDao
import com.example.unsplashapp.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class PhotoRepoImpl @Inject constructor(
    @Named("base_api") private val api: ApiPhoto,
    private val topicsPagingSourceFactory: TopicsPagingSource.Factory,
    private val photosPagingSourceFactory: PhotosPagingSource.Factory,
    private val photoDao: PhotoDao,
    private val mediaManager: MediaManager.Factory
) : PhotoRepository {

    override suspend fun getPhotos(
        topicId: String,
        page: Int,
        pageSize: Int
    ): Response<List<Photo>> {
        return api.getPhotos(topicId, page, pageSize)
    }

    override suspend fun setLike(idPhoto: String, like: Boolean): ViewState {
        return try {
            if (like)
                api.likeAPhoto(idPhoto)
            else
                api.unlikeAPhoto(idPhoto)
            SuccessState
        } catch (e: Exception) {
            ErrorState
        }
    }

    override fun getAllPhotoFromFavoriteCache(): Flow<List<Photo>> {
        return photoDao.getAllFavoritePhoto()
    }

    override suspend fun getLikes(username: String): ApiState<List<Photo>?> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getLikes(username, PAGE, PER_PAGE)
                if (response.isSuccessful) {
                    val body = response.body()
                    photoDao.clearAndAdd(body)
                    Success(body)
                } else Error
            } catch (e: Exception) {
                Error
            }
        }

    override suspend fun getUserInfo(): ApiState<User?> {
        return try {
            val response = api.getUserInfo()
            if (response.isSuccessful) {
                val body = response.body()
                Success(body)
            } else Error
        } catch (e: Exception) {
            Error
        }
    }

    override fun getTopicsPagingSourceFactory(): PagingSource<Int, Topic> {
        return topicsPagingSourceFactory.create()
    }

    override fun getPhotosPagingSourceFactory(query: String): PagingSource<Int, Photo> {
        return photosPagingSourceFactory.create(query)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun downloadPhoto(flagSdk: Boolean, url: String, context: Context): ViewState =
        withContext(Dispatchers.IO) {
            val mediaManager = mediaManager.create(context)
            return@withContext mediaManager.downloadPhoto(flagSdk, url)
        }

    companion object {
        private const val PAGE = 1
        private const val PER_PAGE = 50
    }
}
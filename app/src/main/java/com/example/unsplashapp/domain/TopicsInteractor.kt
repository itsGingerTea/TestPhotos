package com.example.unsplashapp.domain

import androidx.paging.PagingSource
import com.example.unsplashapp.utils.ApiState
import com.example.unsplashapp.utils.Success
import com.example.unsplashapp.data.repository.PhotoRepository
import com.example.unsplashapp.data.repository.PrefsRepository
import com.example.unsplashapp.data.models.Topic
import com.example.unsplashapp.data.models.User
import javax.inject.Inject

class TopicsInteractor @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val prefsRepo: PrefsRepository
) {

    fun getPagingSourceFactory(): PagingSource<Int, Topic> {
        return photoRepository.getTopicsPagingSourceFactory()
    }

    suspend fun getUserInfo(): ApiState<*> {
        val userInfo = photoRepository.getUserInfo()
        when (userInfo) {
            is Success<*> -> saveUserInfo(userInfo.data as User)
            else -> {}
        }
        return userInfo
    }

    fun isLogged(): Boolean {
        return prefsRepo.isLogged()
    }

    fun logout() {
        prefsRepo.clear()
    }

    private fun saveUserInfo(user: User) {
        prefsRepo.saveUserInfo(user)
    }
}
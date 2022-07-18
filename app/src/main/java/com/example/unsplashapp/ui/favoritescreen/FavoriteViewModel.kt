package com.example.unsplashapp.ui.favoritescreen

import androidx.lifecycle.*
import com.example.unsplashapp.*
import com.example.unsplashapp.data.models.Photo
import com.example.unsplashapp.domain.FavoriteInteractor
import com.example.unsplashapp.utils.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    private val interactor: FavoriteInteractor
) : ViewModel() {

    private val _state: MutableLiveData<ApiState<*>> = MutableLiveData()
    val state: LiveData<ApiState<*>> get() = _state
    val snackbar: SingleLiveEvent<Int> = SingleLiveEvent()
    val likeState: SingleLiveEvent<ViewState> = SingleLiveEvent()

    val favoriteLiveData: LiveData<ApiState<*>> = liveData {
        emit(Loading)
        val favoritePhotos = interactor.updatedFavorites
        favoritePhotos.collect { photos ->
            emit(Success(photos))
        }
    }

    fun onViewCreated() {
        loadFavorites()
    }

    fun onRefresh() {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            val username = interactor.getUserInfo().username
            when (val state = interactor.getLikesAndAddToCache(username)) {
                is Success<*> -> _state.postValue(Success(state.data))
                Error -> _state.postValue(Error)
            }
        }
    }

    fun onFavClicked(like: Boolean, photo: Photo) {
        viewModelScope.launch {
            when (interactor.updatePhoto(photo.id, like)) {
                is SuccessState -> {
                    likeState.postValue(SuccessState)
                    if (like) {
                        snackbar.postValue(R.string.snackbar_add_text)
                    } else {
                        snackbar.postValue(R.string.snackbar_delete_text)
                    }
                }
                ErrorState -> likeState.postValue(ErrorState)
            }
        }
    }

}
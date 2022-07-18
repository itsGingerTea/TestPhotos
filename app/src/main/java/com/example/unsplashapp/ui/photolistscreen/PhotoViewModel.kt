package com.example.unsplashapp.ui.photolistscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.unsplashapp.R
import com.example.unsplashapp.data.models.Photo
import com.example.unsplashapp.domain.AuthInteractor
import com.example.unsplashapp.domain.PhotoInteractor
import com.example.unsplashapp.utils.ErrorState
import com.example.unsplashapp.utils.SingleLiveEvent
import com.example.unsplashapp.utils.SuccessState
import com.example.unsplashapp.utils.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

class PhotoViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val photoInteractor: PhotoInteractor
) : ViewModel() {

    val snackbar: SingleLiveEvent<Int> = SingleLiveEvent()
    val state: SingleLiveEvent<ViewState> = SingleLiveEvent()
    private val _flagShowLike: MutableLiveData<Boolean> = MutableLiveData()
    val flagShowLike: LiveData<Boolean> get() = _flagShowLike
    private lateinit var topicId: String

    val photos = Pager(
        config = PagingConfig(10, enablePlaceholders = false),
        pagingSourceFactory = { photoInteractor.getPagingSourceFactory(topicId) }
    ).flow
        .cachedIn(viewModelScope)

    fun onViewCreated(topicId: String) {
        this.topicId = topicId
        val isLogged = authInteractor.isLogged()
        _flagShowLike.postValue(isLogged)
    }

    fun onFavClicked(favorite: Boolean, photo: Photo) = viewModelScope.launch {
        when (photoInteractor.updatePhoto(photo.id, favorite)) {
            is SuccessState -> {
                state.postValue(SuccessState)
                if (favorite) {
                    snackbar.postValue(R.string.snackbar_add_text)
                } else {
                    snackbar.postValue(R.string.snackbar_delete_text)
                }
            }
            ErrorState -> state.postValue(ErrorState)
        }
    }
}

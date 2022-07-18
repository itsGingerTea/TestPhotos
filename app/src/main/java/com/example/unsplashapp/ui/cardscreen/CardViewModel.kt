package com.example.unsplashapp.ui.cardscreen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplashapp.R
import com.example.unsplashapp.domain.PhotoInteractor
import com.example.unsplashapp.utils.ErrorState
import com.example.unsplashapp.utils.SingleLiveEvent
import com.example.unsplashapp.utils.SuccessState
import com.example.unsplashapp.utils.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

class CardViewModel @Inject constructor(
    private val interactor: PhotoInteractor
) : ViewModel() {

    private var id: String? = null
    val snackbar: SingleLiveEvent<Int> = SingleLiveEvent()
    val state: SingleLiveEvent<ViewState> = SingleLiveEvent()
    val loggedUserValue: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _favorite: MutableLiveData<Boolean> = MutableLiveData()
    val favorite: LiveData<Boolean> get() = _favorite

    fun onCreate() {
        loggedUserValue.postValue(interactor.isLogged())
    }

    fun onViewCreated(id: String) {
        this.id = id
    }

    fun onFavoriteClicked(favorite: Boolean) =
        viewModelScope.launch {
            id?.let {
                when (interactor.updatePhoto(it, favorite)) {
                    is SuccessState -> {
                        state.postValue(SuccessState)
                        if (favorite)
                            snackbar.postValue(R.string.snackbar_add_text)
                        else
                            snackbar.postValue(R.string.snackbar_delete_text)
                    }
                    ErrorState -> state.postValue(ErrorState)
                }
                _favorite.postValue(favorite)
            }
        }

    fun onDownloadPhotoClicked(flagSdk: Boolean, url: String, context: Context) {
        viewModelScope.launch {
            when (interactor.downloadPhoto(flagSdk, url, context)) {
                is SuccessState -> snackbar.postValue(R.string.download_info)
                ErrorState -> snackbar.postValue(R.string.title_file_download_exception)
            }
        }
    }
}
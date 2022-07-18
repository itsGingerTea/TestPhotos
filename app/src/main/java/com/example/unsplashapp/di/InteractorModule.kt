package com.example.unsplashapp.di

import com.example.unsplashapp.data.repository.PhotoRepository
import com.example.unsplashapp.data.repository.PrefsRepository
import com.example.unsplashapp.data.repository.TokenRepository
import com.example.unsplashapp.domain.AuthInteractor
import com.example.unsplashapp.domain.FavoriteInteractor
import com.example.unsplashapp.domain.PhotoInteractor
import com.example.unsplashapp.domain.TopicsInteractor
import dagger.Module
import dagger.Provides

@Module
class InteractorModule {

    @Provides
    fun provideAuthInteractor(
        prefsRepository: PrefsRepository,
        tokenRepository: TokenRepository
    ): AuthInteractor {
        return AuthInteractor(prefsRepository, tokenRepository)
    }

    @Provides
    fun providePhotoInteractor(
        photoRepository: PhotoRepository,
        prefsRepository: PrefsRepository
    ): PhotoInteractor {
        return PhotoInteractor(photoRepository, prefsRepository)
    }

    @Provides
    fun provideTopicsInteractor(
        photoRepository: PhotoRepository,
        prefsRepository: PrefsRepository
    ): TopicsInteractor {
        return TopicsInteractor(photoRepository, prefsRepository)
    }

    @Provides
    fun provideFavoriteInteractor(
        photoRepository: PhotoRepository,
        prefsRepository: PrefsRepository
    ): FavoriteInteractor {
        return FavoriteInteractor(photoRepository, prefsRepository)
    }
}

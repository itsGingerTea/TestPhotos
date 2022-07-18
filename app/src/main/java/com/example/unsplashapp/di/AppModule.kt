package com.example.unsplashapp.di

import android.content.Context
import android.content.SharedPreferences
import com.example.unsplashapp.BuildConfig
import com.example.unsplashapp.BuildConfig.BASE_URL
import com.example.unsplashapp.BuildConfig.BASE_URL_AUTH
import com.example.unsplashapp.data.MediaManager
import com.example.unsplashapp.data.PhotosPagingSource
import com.example.unsplashapp.data.TopicsPagingSource
import com.example.unsplashapp.data.network.ApiPhoto
import com.example.unsplashapp.data.network.Interceptor
import com.example.unsplashapp.data.network.LoggedInterceptor
import com.example.unsplashapp.data.repository.*
import com.example.unsplashapp.data.room.PhotoDao
import com.example.unsplashapp.data.room.PhotoDatabase
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor()
    }

    @Provides
    @Singleton
    fun provideLoggedInterceptor(prefsRepo: PrefsRepository): LoggedInterceptor {
        return LoggedInterceptor(prefsRepo)
    }

    @Provides
    @Singleton
    @Named("auth_okhttp")
    fun provideAuthOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        interceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(interceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(40, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("base_okhttp")
    fun provideBaseOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        loggedInterceptor: LoggedInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(loggedInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(40, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("auth")
    fun provideRetrofitAuthService(@Named("auth_okhttp") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_AUTH)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("base")
    fun provideRetrofitService(@Named("base_okhttp") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("auth_api")
    fun provideAuthApiPhoto(@Named("auth") retrofit: Retrofit): ApiPhoto {
        return retrofit.create(ApiPhoto::class.java)
    }

    @Provides
    @Singleton
    @Named("base_api")
    fun provideApiPhoto(@Named("base") retrofit: Retrofit): ApiPhoto {
        return retrofit.create(ApiPhoto::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePrefsRepository(sharedPreferences: SharedPreferences): PrefsRepository {
        return PrefsRepoImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun providePhotoRepository(
        @Named("base_api") apiPhoto: ApiPhoto,
        pagingSourceFactory: TopicsPagingSource.Factory,
        photosPagingSourceFactory: PhotosPagingSource.Factory,
        photoDao: PhotoDao,
        mediaManagerFactory: MediaManager.Factory
    ): PhotoRepository {
        return PhotoRepoImpl(
            apiPhoto,
            pagingSourceFactory,
            photosPagingSourceFactory,
            photoDao,
            mediaManagerFactory
        )
    }

    @Provides
    @Singleton
    fun provideTokenRepository(@Named("auth_api") apiPhoto: ApiPhoto): TokenRepository {
        return TokenRepoImpl(apiPhoto)
    }

    @Provides
    @Singleton
    fun providePhotoDao(context: Context): PhotoDao {
        return PhotoDatabase.getDatabase(context).photoDao()
    }
}
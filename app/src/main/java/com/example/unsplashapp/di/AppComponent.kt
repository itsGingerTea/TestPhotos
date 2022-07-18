package com.example.unsplashapp.di

import android.content.Context
import com.example.unsplashapp.di.authscreen.AuthComponent
import com.example.unsplashapp.di.cardscreen.CardComponent
import com.example.unsplashapp.di.favoritescreen.FavoriteComponent
import com.example.unsplashapp.di.loginscreen.LoginComponent
import com.example.unsplashapp.di.mainscreen.MainComponent
import com.example.unsplashapp.di.photoscreen.PhotoComponent
import com.example.unsplashapp.di.topicsscreen.TopicsComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class,
        InteractorModule::class]
)
interface AppComponent {
    fun mainComponentBuilder(): MainComponent.Builder
    fun photoComponentBuilder(): PhotoComponent.Builder
    fun authComponentBuilder(): AuthComponent.Builder
    fun cardComponentBuilder(): CardComponent.Builder
    fun loginComponentBuilder(): LoginComponent.Builder
    fun topicsComponentBuilder(): TopicsComponent.Builder
    fun favoriteComponentBuilder(): FavoriteComponent.Builder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bindsApplication(context: Context): Builder

        fun build(): AppComponent
    }
}
package com.example.unsplashapp.di.favoritescreen

import android.view.LayoutInflater
import com.example.unsplashapp.ui.favoritescreen.FavoriteFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FavoriteScope
@Subcomponent
interface FavoriteComponent {

    fun inject(fragment: FavoriteFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun bindInflater(inflater: LayoutInflater): Builder

        fun build(): FavoriteComponent
    }
}
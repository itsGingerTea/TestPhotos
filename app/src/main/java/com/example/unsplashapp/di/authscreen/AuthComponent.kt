package com.example.unsplashapp.di.authscreen

import android.view.LayoutInflater
import com.example.unsplashapp.ui.authscreen.AuthUnsplashFragment
import dagger.BindsInstance
import dagger.Subcomponent

@AuthScope
@Subcomponent
interface AuthComponent {

    fun inject(fragment: AuthUnsplashFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun bindInflater(inflater: LayoutInflater): Builder

        fun build(): AuthComponent
    }
}
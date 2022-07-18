package com.example.unsplashapp.di.loginscreen

import android.view.LayoutInflater
import com.example.unsplashapp.ui.loginscreen.LoginUnsplashFragment
import dagger.BindsInstance
import dagger.Subcomponent

@LoginScope
@Subcomponent
interface LoginComponent {

    fun inject(fragment: LoginUnsplashFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun bindsInflater(inflater: LayoutInflater): Builder

        fun build(): LoginComponent
    }
}
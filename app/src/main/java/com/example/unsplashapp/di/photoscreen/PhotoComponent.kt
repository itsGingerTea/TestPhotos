package com.example.unsplashapp.di.photoscreen

import android.view.LayoutInflater
import com.example.unsplashapp.ui.photolistscreen.PhotoFragment
import dagger.BindsInstance
import dagger.Subcomponent

@PhotoScope
@Subcomponent
interface PhotoComponent {

    fun inject(fragment: PhotoFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun bindInflater(inflater: LayoutInflater): Builder

        fun build(): PhotoComponent
    }
}
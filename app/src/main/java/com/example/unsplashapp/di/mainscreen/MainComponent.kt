package com.example.unsplashapp.di.mainscreen

import android.view.LayoutInflater
import com.example.unsplashapp.ui.mainscreen.MainActivity
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
interface MainComponent {

    fun inject(activity: MainActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun bindInflater(inflater: LayoutInflater): Builder

        fun build(): MainComponent
    }
}
package com.example.unsplashapp.di.topicsscreen

import android.view.LayoutInflater
import com.example.unsplashapp.ui.topicsscreen.TopicsFragment
import dagger.BindsInstance
import dagger.Subcomponent

@TopicsScope
@Subcomponent
interface TopicsComponent {

    fun inject(fragment: TopicsFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun bindInflater(inflater: LayoutInflater): Builder

        fun build(): TopicsComponent
    }
}
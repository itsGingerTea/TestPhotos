package com.example.unsplashapp.di.cardscreen

import android.view.LayoutInflater
import com.example.unsplashapp.ui.cardscreen.CardFragment
import dagger.BindsInstance
import dagger.Subcomponent

@CardScope
@Subcomponent
interface CardComponent {

    fun inject(fragment: CardFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun bindInflater(inflater: LayoutInflater): Builder

        fun build(): CardComponent
    }
}
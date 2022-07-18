package com.example.unsplashapp

import android.app.Application
import com.example.unsplashapp.di.AppComponent
import com.example.unsplashapp.di.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().bindsApplication(this).build()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}
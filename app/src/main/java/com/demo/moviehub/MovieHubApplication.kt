package com.demo.moviehub

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovieHubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any application-wide components here
    }
}

// This is needed for Hilt to work with the application class
@Suppress("unused")
val applicationInjector = listOf(MovieHubApplication::class.java)

package com.demo.moviehub

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovieHubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

@Suppress("unused")
val applicationInjector = listOf(MovieHubApplication::class.java)

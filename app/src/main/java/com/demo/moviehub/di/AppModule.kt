package com.demo.moviehub.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Add your application-wide dependencies here
    
    @Provides
    @Singleton
    fun provideSomeDependency(): String {
        return "Hello from Hilt!"
    }
}

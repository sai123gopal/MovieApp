package com.demo.moviehub.di

import android.content.Context
import com.demo.moviehub.data.cache.MovieCache
import com.demo.moviehub.data.cache.dao.CachedMovieDao
import com.demo.moviehub.data.network.TmdbApiService
import com.demo.moviehub.data.repository.MovieRepository
import com.demo.moviehub.data.repository.MovieRepositoryImpl
import com.demo.moviehub.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    
    companion object {
        @Provides
        @Singleton
        fun provideNetworkMonitor(
            @ApplicationContext context: Context
        ): NetworkMonitor = NetworkMonitor(context)

        @Provides
        @Singleton
        fun provideMovieCache(
            cachedMovieDao: CachedMovieDao,
            networkMonitor: NetworkMonitor
        ): MovieCache = MovieCache(cachedMovieDao, networkMonitor)
    }
    
    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository
}

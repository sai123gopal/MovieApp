package com.demo.moviehub.di

import android.content.Context
import com.demo.moviehub.data.local.MovieDatabase
import com.demo.moviehub.data.local.FavoriteMovieDao
import com.demo.moviehub.data.repository.FavoriteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MovieDatabase = MovieDatabase.getDatabase(context)
    
    @Provides
    fun provideFavoriteMovieDao(database: MovieDatabase): FavoriteMovieDao {
        return database.favoriteMovieDao()
    }
    
    @Provides
    @Singleton
    fun provideFavoriteRepository(favoriteMovieDao: FavoriteMovieDao): FavoriteRepository {
        return FavoriteRepository(favoriteMovieDao)
    }
}

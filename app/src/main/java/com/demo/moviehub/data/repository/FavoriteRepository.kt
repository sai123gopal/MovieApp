package com.demo.moviehub.data.repository

import com.demo.moviehub.data.local.FavoriteMovie
import com.demo.moviehub.data.local.FavoriteMovieDao
import com.demo.moviehub.data.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao
) {
    val allFavoriteMovies: Flow<List<FavoriteMovie>> = favoriteMovieDao.getAllFavoriteMovies()
    
    suspend fun addToFavorites(movie: Movie) {
        favoriteMovieDao.insertMovie(FavoriteMovie.fromMovie(movie))
    }
    
    suspend fun removeFromFavorites(movie: Movie) {
        favoriteMovieDao.deleteMovie(FavoriteMovie.fromMovie(movie))
    }
    
    suspend fun isFavorite(movieId: Int): Boolean {
        return favoriteMovieDao.isMovieFavorite(movieId) > 0
    }
    
    fun getFavoriteMovies(): Flow<List<FavoriteMovie>> {
        return favoriteMovieDao.getAllFavoriteMovies()
    }
}

package com.demo.moviehub.data.repository

import com.demo.moviehub.data.local.FavoriteMovie
import com.demo.moviehub.data.local.FavoriteMovieDao
import com.demo.moviehub.data.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface BaseFavoriteRepository {
    val allFavoriteMovies: Flow<List<FavoriteMovie>>
    suspend fun addToFavorites(movie: Movie)
    suspend fun removeFromFavorites(movie: Movie)
    suspend fun removeFromFavorites(movieId: Int)
    suspend fun isFavorite(movieId: Int): Boolean
    fun getFavoriteMovies(): Flow<List<FavoriteMovie>>
}

@Singleton
class FavoriteRepository @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao
) : BaseFavoriteRepository {
    override val allFavoriteMovies: Flow<List<FavoriteMovie>> = favoriteMovieDao.getAllFavoriteMovies()
    
    override suspend fun addToFavorites(movie: Movie) {
        favoriteMovieDao.insertMovie(FavoriteMovie.fromMovie(movie))
    }
    
    override suspend fun removeFromFavorites(movie: Movie) {
        favoriteMovieDao.deleteMovie(FavoriteMovie.fromMovie(movie))
    }
    
    override suspend fun removeFromFavorites(movieId: Int) {
        favoriteMovieDao.deleteMovieById(movieId)
    }
    
    override suspend fun isFavorite(movieId: Int): Boolean {
        return favoriteMovieDao.isMovieFavorite(movieId) > 0
    }
    
    override fun getFavoriteMovies(): Flow<List<FavoriteMovie>> {
        return favoriteMovieDao.getAllFavoriteMovies()
    }
}

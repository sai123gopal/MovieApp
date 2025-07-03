package com.demo.moviehub.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: FavoriteMovie)
    
    @Delete
    suspend fun deleteMovie(movie: FavoriteMovie)
    
    @Query("SELECT * FROM favorite_movies ORDER BY id DESC")
    fun getAllFavoriteMovies(): Flow<List<FavoriteMovie>>
    
    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    suspend fun getFavoriteMovieById(movieId: Int): FavoriteMovie?
    
    @Query("SELECT COUNT(*) FROM favorite_movies WHERE id = :movieId")
    suspend fun isMovieFavorite(movieId: Int): Int
}

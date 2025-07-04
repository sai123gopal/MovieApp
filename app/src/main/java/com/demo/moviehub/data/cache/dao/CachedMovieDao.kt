package com.demo.moviehub.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.moviehub.data.cache.entity.CachedMovie

@Dao
interface CachedMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<CachedMovie>)

    @Query("SELECT * FROM cached_movies WHERE cacheType = :cacheType AND cacheKey = :cacheKey ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestByCacheTypeAndKey(cacheType: String, cacheKey: String): List<CachedMovie>

    @Query("DELETE FROM cached_movies WHERE cacheType = :cacheType AND cacheKey = :cacheKey")
    suspend fun deleteByCacheTypeAndKey(cacheType: String, cacheKey: String)

    @Query("SELECT * FROM cached_movies WHERE cacheType = :cacheType AND cacheKey = :cacheKey")
    suspend fun getByCacheTypeAndKey(cacheType: String, cacheKey: String): List<CachedMovie>
}

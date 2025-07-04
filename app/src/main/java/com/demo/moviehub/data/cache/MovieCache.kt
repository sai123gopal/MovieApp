package com.demo.moviehub.data.cache

import android.content.Context
import com.demo.moviehub.data.cache.dao.CachedMovieDao
import com.demo.moviehub.data.cache.entity.CachedMovie
import com.demo.moviehub.data.model.Movie
import com.demo.moviehub.util.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieCache @Inject constructor(
    private val dao: CachedMovieDao,
    private val networkMonitor: NetworkMonitor
) {
    private val inMemoryTrendingCache = mutableMapOf<String, List<Movie>>()
    private val inMemoryPopularCache = mutableMapOf<String, List<Movie>>()
    
    companion object {
        private const val CACHE_MAX_AGE_DAYS = 7L // Cache duration in days
        private const val CACHE_TYPE_TRENDING = "trending"
        private const val CACHE_TYPE_POPULAR = "popular"
    }
    
    suspend fun getTrendingMovies(timeWindow: String): Flow<List<Movie>> = flow {
        // First check in-memory cache
        inMemoryTrendingCache[timeWindow]?.let { movies ->
            emit(movies)
            return@flow
        }
        
        // Then check database cache
        val cachedMovies = dao.getByCacheTypeAndKey(CACHE_TYPE_TRENDING, timeWindow)
            .map { it.toMovie() }
            .takeIf { it.isNotEmpty() }
        
        cachedMovies?.let { movies ->
            inMemoryTrendingCache[timeWindow] = movies
            emit(movies)
        }
    }
    
    suspend fun cacheTrendingMovies(timeWindow: String, movies: List<Movie>) {
        // Update in-memory cache
        inMemoryTrendingCache[timeWindow] = movies
        
        // Update database cache
        val cachedMovies = movies.map { 
            CachedMovie.fromMovie(it, CACHE_TYPE_TRENDING, timeWindow) 
        }
        dao.deleteByCacheTypeAndKey(CACHE_TYPE_TRENDING, timeWindow)
        dao.insertMovies(cachedMovies)
    }
    
    suspend fun getPopularMovies(fromDate: String, toDate: String): Flow<List<Movie>> = flow {
        val cacheKey = "$fromDate-$toDate"
        
        // First check in-memory cache
        inMemoryPopularCache[cacheKey]?.let { movies ->
            emit(movies)
            return@flow
        }
        
        // Then check database cache
        val cachedMovies = dao.getByCacheTypeAndKey(CACHE_TYPE_POPULAR, cacheKey)
            .map { it.toMovie() }
            .takeIf { it.isNotEmpty() }
        
        cachedMovies?.let { movies ->
            inMemoryPopularCache[cacheKey] = movies
            emit(movies)
        }
    }
    
    suspend fun cachePopularMovies(fromDate: String, toDate: String, movies: List<Movie>) {
        val cacheKey = "$fromDate-$toDate"
        
        // Update in-memory cache
        inMemoryPopularCache[cacheKey] = movies
        
        // Update database cache
        val cachedMovies = movies.map { 
            CachedMovie.fromMovie(it, CACHE_TYPE_POPULAR, cacheKey) 
        }
        dao.deleteByCacheTypeAndKey(CACHE_TYPE_POPULAR, cacheKey)
        dao.insertMovies(cachedMovies)
    }
    
    private suspend fun isCacheValid(timestamp: Long): Boolean {
        val cacheAge = System.currentTimeMillis() - timestamp
        val maxAge = TimeUnit.DAYS.toMillis(CACHE_MAX_AGE_DAYS)
        return cacheAge < maxAge
    }
}

package com.demo.moviehub.data.repository

import com.demo.moviehub.data.model.MovieResponse
import com.demo.moviehub.data.network.TmdbApiService
import com.demo.moviehub.util.Constants.API_KEY
import javax.inject.Inject

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Result<MovieResponse>
    suspend fun getTrendingMovies(page: Int): Result<MovieResponse>
}

class MovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService
) : MovieRepository {
    
    override suspend fun getPopularMovies(page: Int): Result<MovieResponse> {
        return try {
            val response = apiService.getPopularMovies(page = page)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTrendingMovies(page: Int): Result<MovieResponse> {
        return try {
            val response = apiService.getTrendingMovies(page = page)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

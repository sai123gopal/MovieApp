package com.demo.moviehub.data.repository

import com.demo.moviehub.data.model.MovieResponse
import com.demo.moviehub.data.network.TmdbApiService
import com.demo.moviehub.util.Constants.API_KEY
import javax.inject.Inject

interface MovieRepository {
    suspend fun getPopularMovies(
        page: Int = 1,
        fromDate: String? = null,
        toDate: String? = null
    ): Result<MovieResponse>
    
    suspend fun getTrendingMovies(
        timeWindow: String = "day", 
        page: Int = 1
    ): Result<MovieResponse>
}

class MovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService
) : MovieRepository {
    
    override suspend fun getPopularMovies(
        page: Int,
        fromDate: String?,
        toDate: String?
    ): Result<MovieResponse> {
        return try {
            val response = apiService.getPopularMovies(
                page = page,
                fromDate = fromDate,
                toDate = toDate
            )
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTrendingMovies(
        timeWindow: String,
        page: Int
    ): Result<MovieResponse> {
        return try {
            val response = apiService.getTrendingMovies(
                timeWindow = timeWindow,
                page = page
            )
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } 
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

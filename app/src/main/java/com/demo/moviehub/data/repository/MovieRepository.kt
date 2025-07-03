package com.demo.moviehub.data.repository

import com.demo.moviehub.data.model.MovieDetails
import com.demo.moviehub.data.model.MovieResponse
import com.demo.moviehub.data.network.TmdbApiService
import com.demo.moviehub.util.Result
import java.io.IOException
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
    
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails>
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
                response.body()?.let { Result.Success(it) } ?: Result.Error(Exception("Empty response"))
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
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
                response.body()?.let { Result.Success(it) }
                    ?: Result.Error(Exception("Empty response"))
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetails> {
        return try {
            val response = apiService.getMovieDetails(movieId = movieId)
            if (response.isSuccessful) {
                response.body()?.let { Result.Success(it) } 
                    ?: Result.Error(IOException("Empty response"))
            } else {
                Result.Error(IOException(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

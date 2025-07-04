package com.demo.moviehub.data.repository

import com.demo.moviehub.data.cache.MovieCache
import com.demo.moviehub.data.model.MovieDetails
import com.demo.moviehub.data.model.MovieResponse
import com.demo.moviehub.data.network.TmdbApiService
import com.demo.moviehub.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

interface MovieRepository {
    suspend fun getPopularMovies(
        page: Int = 1,
        fromDate: String? = null,
        toDate: String? = null
    ): Flow<Result<MovieResponse>>
    
    suspend fun getTrendingMovies(
        timeWindow: String = "day", 
        page: Int = 1
    ): Flow<Result<MovieResponse>>
    
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails>
}

class MovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService,
    private val movieCache: MovieCache
) : MovieRepository {
    
    override suspend fun getPopularMovies(
        page: Int,
        fromDate: String?,
        toDate: String?
    ): Flow<Result<MovieResponse>> = flow {

        if (fromDate != null && toDate != null) {

            val cachedMovies = movieCache.getPopularMovies(fromDate, toDate)
                .firstOrNull()

            try {
                cachedMovies?.let { movies ->
                    emit(Result.Success(MovieResponse(
                        page = 1,
                        results = movies,
                        totalPages = 1,
                        totalResults = movies.size
                    )))
                }
            } catch (_: Exception) { }

            try {
                val response = apiService.getPopularMovies(
                    page = page,
                    fromDate = fromDate,
                    toDate = toDate
                )

                if (response.isSuccessful) {
                    response.body()?.let { movieResponse ->
                        movieCache.cachePopularMovies(fromDate, toDate, movieResponse.results)
                        emit(Result.Success(movieResponse))
                    } ?: emit(Result.Error(Exception("Empty response")))
                } else {
                    emit(Result.Error(Exception(response.message())))
                }
            } catch (e: Exception) {
                if (cachedMovies == null) {
                    emit(Result.Error(e))
                }

            }
        }


    }

    override suspend fun getTrendingMovies(
        timeWindow: String,
        page: Int
    ): Flow<Result<MovieResponse>> = flow {
        val cachedMovies = movieCache.getTrendingMovies(timeWindow)
            .firstOrNull()
        try {
            cachedMovies?.let { movies ->
                emit(Result.Success(MovieResponse(
                    page = 1,
                    results = movies,
                    totalPages = 1,
                    totalResults = movies.size
                )))
            }
        } catch (_: Exception) { }

        try {
            val response = apiService.getTrendingMovies(
                timeWindow = timeWindow,
                page = page
            )
            
            if (response.isSuccessful) {
                response.body()?.let { movieResponse ->
                    movieCache.cacheTrendingMovies(timeWindow, movieResponse.results)
                    emit(Result.Success(movieResponse))
                } ?: emit(Result.Error(Exception("Empty response")))
            } else {
                emit(Result.Error(Exception(response.message())))
            }
        } catch (e: Exception) {
            if (cachedMovies  == null) {
                emit(Result.Error(e))
            }
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

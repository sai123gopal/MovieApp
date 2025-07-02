package com.demo.moviehub.network

import com.demo.moviehub.util.Result
import retrofit2.HttpException
import java.io.IOException

/**
 * Maps network call result to [Result] type.
 */
suspend fun <T : Any, R : Any> Result<T>.mapToResult(
    success: suspend (T) -> R
): Result<R> {
    return when (this) {
        is Result.Success -> {
            try {
                Result.Success(success(data))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
        is Result.Error -> this
        is Result.Loading -> this
    }
}

/**
 * Handles API call and returns [Result].
 */
suspend fun <T : Any> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return try {
        Result.Success(apiCall())
    } catch (e: Exception) {
        when (e) {
            is HttpException -> Result.Error(e)
            is IOException -> Result.Error(IOException("Network error. Please check your internet connection.", e))
            else -> Result.Error(IOException("An unexpected error occurred.", e))
        }
    }
}

/**
 * Handles API call and returns [Result] with null check.
 */
suspend fun <T : Any> safeApiCallWithNullCheck(apiCall: suspend () -> T?): Result<T> {
    return try {
        val response = apiCall()
        if (response != null) {
            Result.Success(response)
        } else {
            Result.Error(NullPointerException("Response is null"))
        }
    } catch (e: Exception) {
        when (e) {
            is HttpException -> Result.Error(e)
            is IOException -> Result.Error(IOException("Network error. Please check your internet connection.", e))
            else -> Result.Error(IOException("An unexpected error occurred.", e))
        }
    }
}

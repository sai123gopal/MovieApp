package com.demo.moviehub.network

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class ApiResponse<out T> {

    data class Success<out T>(val data: T) : ApiResponse<T>()


    data class Error(
        val code: Int? = null,
        val message: String? = null,
        val errorBody: String? = null
    ) : ApiResponse<Nothing>()

    object Loading : ApiResponse<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[code=$code, message=$message, errorBody=$errorBody]"
            Loading -> "Loading"
        }
    }

    companion object {

        fun <T> create(response: retrofit2.Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    Error(
                        code = response.code(),
                        message = "Empty response body"
                    )
                } else {
                    Success(body)
                }
            } else {
                val errorBody = try {
                    response.errorBody()?.string()
                } catch (e: Exception) {
                    null
                }
                Error(
                    code = response.code(),
                    message = response.message(),
                    errorBody = errorBody
                )
            }
        }

        fun <T> create(throwable: Throwable): ApiResponse<T> {
            return when (throwable) {
                is HttpException -> {
                    val errorBody = try {
                        throwable.response()?.errorBody()?.string()
                    } catch (e: Exception) {
                        null
                    }
                    Error(
                        code = throwable.code(),
                        message = throwable.message(),
                        errorBody = errorBody
                    )
                }
                is SocketTimeoutException -> {
                    Error(message = "Connection timeout. Please check your internet connection and try again.")
                }
                is UnknownHostException -> {
                    Error(message = "No internet connection. Please check your network settings and try again.")
                }
                is IOException -> {
                    Error(message = "Network error occurred. Please try again later.")
                }
                else -> {
                    Error(message = throwable.message ?: "An unknown error occurred")
                }
            }
        }
    }
}




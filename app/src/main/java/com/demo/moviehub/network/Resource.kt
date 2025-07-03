package com.demo.moviehub.network

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val throwable: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null, throwable: Throwable? = null) :
        Resource<T>(data, message, throwable)

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[message=$message, throwable=$throwable]"
            is Loading<T> -> "Loading"
        }
    }
}


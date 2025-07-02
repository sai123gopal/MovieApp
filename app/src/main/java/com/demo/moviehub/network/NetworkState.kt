package com.demo.moviehub.network

import com.demo.moviehub.util.Result

/**
 * A generic class that holds a value with its loading status.
 * @param status The status of the current data.
 * @param data The data to be displayed.
 * @param error The error message if an error occurred.
 */
data class NetworkState<out T>(
    val status: Status,
    val data: T? = null,
    val error: Throwable? = null
) {
    companion object {
        /**
         * Creates a [NetworkState] object with the status [Status.SUCCESS] and the provided data.
         * @param data The data to be displayed.
         */
        fun <T> success(data: T?): NetworkState<T> {
            return NetworkState(Status.SUCCESS, data, null)
        }

        /**
         * Creates a [NetworkState] object with the status [Status.ERROR] and the provided error.
         * @param error The error that occurred.
         */
        fun <T> error(error: Throwable, data: T? = null): NetworkState<T> {
            return NetworkState(Status.ERROR, data, error)
        }

        /**
         * Creates a [NetworkState] object with the status [Status.LOADING].
         */
        fun <T> loading(data: T? = null): NetworkState<T> {
            return NetworkState(Status.LOADING, data, null)
        }
    }

    /**
     * Status of a resource that is provided to the UI.
     */
    enum class Status {
        /**
         * The request is in progress.
         */
        LOADING,

        /**
         * The request was successful.
         */
        SUCCESS,

        /**
         * The request failed.
         */
        ERROR
    }
}

/**
 * Converts a [Result] to a [NetworkState].
 */
fun <T : Any> Result<T>.toNetworkState(): NetworkState<T> {
    return when (this) {
        is Result.Success -> NetworkState.success(data)
        is Result.Error -> NetworkState.error(exception)
        is Result.Loading -> NetworkState.loading()
    }
}

/**
 * Maps the data of a [NetworkState] using the provided [transform] function.
 */
fun <T : Any, R : Any> NetworkState<T>.map(transform: (T) -> R): NetworkState<R> {
    return when (this.status) {
        NetworkState.Status.SUCCESS -> {
            val transformedData = data?.let { transform(it) }
            NetworkState.success(transformedData)
        }
        NetworkState.Status.ERROR -> NetworkState.error(error!!, data?.let { transform(it) })
        NetworkState.Status.LOADING -> NetworkState.loading(data?.let { transform(it) })
    }
}

/**
 * Returns the data if the [NetworkState] is [NetworkState.Status.SUCCESS], otherwise returns null.
 */
fun <T> NetworkState<T>.getDataIfSuccess(): T? {
    return if (status == NetworkState.Status.SUCCESS) data else null
}

/**
 * Returns true if the [NetworkState] is [NetworkState.Status.LOADING].
 */
val NetworkState<*>.isLoading: Boolean
    get() = status == NetworkState.Status.LOADING

/**
 * Returns true if the [NetworkState] is [NetworkState.Status.SUCCESS].
 */
val NetworkState<*>.isSuccess: Boolean
    get() = status == NetworkState.Status.SUCCESS


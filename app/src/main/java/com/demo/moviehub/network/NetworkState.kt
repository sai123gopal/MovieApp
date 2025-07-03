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

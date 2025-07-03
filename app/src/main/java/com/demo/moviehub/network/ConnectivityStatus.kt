package com.demo.moviehub.network

/**
 * A sealed class representing the current network connectivity status.
 */
sealed class ConnectivityStatus {
    object Available : ConnectivityStatus()
    object Unavailable : ConnectivityStatus()
    object Losing : ConnectivityStatus()
    object Lost : ConnectivityStatus()
}


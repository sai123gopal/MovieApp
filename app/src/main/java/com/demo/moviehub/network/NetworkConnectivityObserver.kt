package com.demo.moviehub.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.core.content.getSystemService
import com.demo.moviehub.util.NetworkUtils
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

interface ConnectivityObserver {
    fun observe(): Flow<NetworkStatus>

    enum class NetworkStatus {
        Available, Unavailable, Losing, Lost
    }
}

class NetworkConnectivityObserver(
    private val context: Context
) : ConnectivityObserver {

    private val connectivityManager = context.getSystemService<ConnectivityManager>()

    override fun observe(): Flow<ConnectivityObserver.NetworkStatus> {
        return callbackFlow {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val networkCallback = object : NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        trySend(ConnectivityObserver.NetworkStatus.Available)
                    }

                    override fun onLosing(network: Network, maxMsToLive: Int) {
                        super.onLosing(network, maxMsToLive)
                        trySend(ConnectivityObserver.NetworkStatus.Losing)
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        trySend(ConnectivityObserver.NetworkStatus.Lost)
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        trySend(ConnectivityObserver.NetworkStatus.Unavailable)
                    }
                }

                val request = NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build()

                connectivityManager?.registerNetworkCallback(request, networkCallback)

                // Initial check
                val currentStatus = if (NetworkUtils.isNetworkAvailable(context)) {
                    ConnectivityObserver.NetworkStatus.Available
                } else {
                    ConnectivityObserver.NetworkStatus.Unavailable
                }
                trySend(currentStatus)

                awaitClose {
                    connectivityManager?.unregisterNetworkCallback(networkCallback)
                }
            } else {
                // For older versions, use a simple polling mechanism
                var currentStatus = if (NetworkUtils.isNetworkAvailable(context)) {
                    ConnectivityObserver.NetworkStatus.Available
                } else {
                    ConnectivityObserver.NetworkStatus.Unavailable
                }
                
                trySend(currentStatus)
                
                awaitClose {}
            }
        }.distinctUntilChanged()
    }
}

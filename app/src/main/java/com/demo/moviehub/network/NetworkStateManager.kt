package com.demo.moviehub.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NetworkStateManager @Inject constructor(
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    val networkStatus: StateFlow<NetworkStatus> = connectivityObserver.observe()
        .map { status ->
            when (status) {
                ConnectivityObserver.NetworkStatus.Available -> NetworkStatus.Available
                ConnectivityObserver.NetworkStatus.Unavailable -> NetworkStatus.Unavailable
                ConnectivityObserver.NetworkStatus.Losing -> NetworkStatus.Losing
                ConnectivityObserver.NetworkStatus.Lost -> NetworkStatus.Lost
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NetworkStatus.Unknown
        )

    enum class NetworkStatus {
        Available, Unavailable, Losing, Lost, Unknown
    }
}

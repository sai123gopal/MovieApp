package com.demo.moviehub.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.demo.moviehub.R
import com.demo.moviehub.network.NetworkState

/**
 * A composable that displays the current network state.
 *
 * @param networkState The current network state.
 * @param onRetry The callback to invoke when the retry button is clicked.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun NetworkStateView(
    networkState: NetworkState<*>,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    when (networkState.status) {
        NetworkState.Status.LOADING -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        NetworkState.Status.ERROR -> {
            ErrorView(
                message = networkState.error?.message ?: stringResource(R.string.error_unknown),
                onRetry = onRetry,
                modifier = modifier
            )
        }
        else -> { /* No-op */ }
    }
}

/**
 * A composable that displays an error message with an optional retry button.
 *
 * @param message The error message to display.
 * @param onRetry The callback to invoke when the retry button is clicked.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun ErrorView(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}

/**
 * A composable that displays a loading indicator.
 *
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun LoadingView(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * A composable that displays an empty state.
 *
 * @param message The message to display.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun EmptyView(
    message: String = stringResource(R.string.no_data_available),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

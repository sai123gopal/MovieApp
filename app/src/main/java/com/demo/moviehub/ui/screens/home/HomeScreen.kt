package com.demo.moviehub.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.moviehub.ui.components.MovieItem
import com.demo.moviehub.ui.theme.YellowRating
import com.demo.moviehub.util.NetworkMonitor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMovieClick: (Int) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
    networkMonitor : NetworkMonitor
) {
    var selectedFilter by remember { mutableIntStateOf(0) }
    val uiState by viewModel.uiState.collectAsState()
    val isConnected by networkMonitor.isConnected.collectAsState(initial = true)

    val currentDate = remember { java.time.LocalDate.now().toString() }
    val thirtyDaysAgo = remember { java.time.LocalDate.now().minusDays(30).toString() }
    val weekAgo = remember { java.time.LocalDate.now().minusWeeks(1).toString() }
    
    LaunchedEffect(selectedFilter) {
        when (selectedFilter) {
            0 -> {
                viewModel.loadTrendingMovies(timeWindow = "day")
                viewModel.loadPopularMovies(
                    fromDate = currentDate,
                    toDate = currentDate
                )
            }
            1 -> {
                viewModel.loadTrendingMovies(timeWindow = "week")
                viewModel.loadPopularMovies(
                    fromDate = weekAgo,
                    toDate = currentDate
                )
            }
            2 -> {
                viewModel.loadTrendingMovies(timeWindow = "day") // Keep as day to get latest
                viewModel.loadPopularMovies(
                    fromDate = thirtyDaysAgo,
                    toDate = currentDate
                )
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {

        TopAppBar(
            title = { 
                Text("TMDB Movie")
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == 0,
                    onClick = { selectedFilter = 0 },
                    label = { Text("Today") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = YellowRating.copy(alpha = 0.2f),
                        selectedLabelColor = YellowRating
                    )
                )
                
                FilterChip(
                    selected = selectedFilter == 1,
                    onClick = { selectedFilter = 1 },
                    label = { Text("This Week") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = YellowRating.copy(alpha = 0.2f),
                        selectedLabelColor = YellowRating
                    )
                )
                
                FilterChip(
                    selected = selectedFilter == 2,
                    onClick = { selectedFilter = 2 },
                    label = { Text("This Month") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = YellowRating.copy(alpha = 0.2f),
                        selectedLabelColor = YellowRating
                    )
                )
            }

            Text(
                text = "Trending",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (uiState.isLoading && uiState.trendingMovies.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if(isConnected.not()) "No internet connection" else "Error loading trending movies: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            } else if (uiState.trendingMovies.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.trendingMovies) { movie ->
                        MovieItem(
                            movie = movie,
                            isFavorite = uiState.favoriteMovieIds.contains(movie.id),
                            onItemClick = { onMovieClick(movie.id) },
                            onFavoriteClick = { viewModel.toggleFavorite(movie) }
                        )
                    }
                }
            }

            Text(
                text = "Popular Movies",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            if (uiState.isLoading && uiState.popularMovies.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if(isConnected.not()) "No internet connection" else "Error loading popular movies: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyRow(
                    modifier = Modifier.padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.popularMovies) { movie ->
                        MovieItem(
                            movie = movie,
                            modifier = Modifier.width(150.dp),
                            isFavorite = uiState.favoriteMovieIds.contains(movie.id),
                            onItemClick = { onMovieClick(movie.id) },
                            onFavoriteClick = { viewModel.toggleFavorite(movie) }
                        )
                    }
                }
            }
        }
    }
}

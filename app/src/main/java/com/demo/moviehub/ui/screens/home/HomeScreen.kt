package com.demo.moviehub.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.moviehub.R
import com.demo.moviehub.ui.components.MoviesList
import com.demo.moviehub.ui.components.SectionHeader

@Composable
fun HomeScreen(
    onMovieClick: (Int) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        viewModel.loadTrendingMovies()
        viewModel.loadPopularMovies()
    }
    
    if (uiState.error != null) {
        // Show error message
        Text(
            text = "Error: ${uiState.error}",
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxSize()
        )
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Trending Movies Section
        SectionHeader(
            title = stringResource(id = R.string.trending_movies),
            onViewAllClick = { /* Handle view all trending */ }
        )
        
        if (uiState.trendingMovies.isEmpty() && uiState.isLoading) {
            // Show loading indicator for trending movies
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .wrapContentWidth()
            )
        } else {
            MoviesList(
                movies = uiState.trendingMovies,
                onMovieClick = onMovieClick
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Popular Movies Section
        SectionHeader(
            title = stringResource(id = R.string.popular_movies),
            onViewAllClick = { /* Handle view all popular */ }
        )
        
        if (uiState.popularMovies.isEmpty() && uiState.isLoading) {
            // Show loading indicator for popular movies
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .wrapContentWidth()
            )
        } else {
            MoviesList(
                movies = uiState.popularMovies,
                onMovieClick = onMovieClick
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

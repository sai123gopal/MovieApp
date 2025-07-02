package com.demo.moviehub.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.demo.moviehub.data.model.Movie

@Composable
fun MoviesList(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(movies) { movie ->
            MovieItem(
                movie = movie,
                onItemClick = onMovieClick
            )
        }
    }
}

package com.demo.moviehub.ui.screens.details

import androidx.compose.runtime.State
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.moviehub.data.model.Movie
import com.demo.moviehub.data.model.MovieDetails
import com.demo.moviehub.data.repository.FavoriteRepository
import com.demo.moviehub.data.repository.MovieRepository
import com.demo.moviehub.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val favoriteRepository: FavoriteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _movieDetails = MutableStateFlow<Result<MovieDetails>>(Result.Loading)
    val movieDetails: StateFlow<Result<MovieDetails>> = _movieDetails

    var movieDetailsData: MovieDetails? = null

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val movieId: Int? = savedStateHandle.get<Int>("movieId")

    init {
        movieId?.let { 
            fetchMovieDetails(it)
            checkIfFavorite(it)
        }
    }

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _movieDetails.value = Result.Loading
            try {
                val result = repository.getMovieDetails(movieId)
                _movieDetails.value = result
            } catch (e: Exception) {
                _movieDetails.value = Result.Error(e)
            }
        }
    }
    
    private fun checkIfFavorite(movieId: Int) {
        viewModelScope.launch {
            _isFavorite.value = favoriteRepository.isFavorite(movieId)
        }
    }
    
    suspend fun toggleFavorite() {
        val current = _isFavorite.value
        _isFavorite.value = !current
        
        movieId?.let { id ->
            if (current) {
                favoriteRepository.removeFromFavorites(id)
            } else {
                val movieDetails = movieDetailsData ?: return@let
                val movie = Movie(
                    id = movieDetails.id,
                    title = movieDetails.title,
                    overview = movieDetails.overview,
                    posterPath = movieDetails.posterPath,
                    backdropPath = movieDetails.backdropPath,
                    releaseDate = movieDetails.releaseDate,
                    voteAverage = movieDetails.voteAverage,
                    voteCount = movieDetails.voteCount,
                    popularity = movieDetails.popularity,
                )
                favoriteRepository.addToFavorites(movie)
            }
            // Refresh the favorite status
            _isFavorite.value = favoriteRepository.isFavorite(id)
        }
    }
}

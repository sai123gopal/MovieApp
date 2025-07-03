package com.demo.moviehub.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.moviehub.data.model.MovieDetails
import com.demo.moviehub.data.repository.MovieRepository
import com.demo.moviehub.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _movieDetails = MutableStateFlow<Result<MovieDetails>>(Result.Loading)
    val movieDetails: StateFlow<Result<MovieDetails>> = _movieDetails

    init {
        val movieId = savedStateHandle.get<Int>("movieId")
        movieId?.let { fetchMovieDetails(it) }
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
}

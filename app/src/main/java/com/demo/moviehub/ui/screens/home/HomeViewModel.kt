package com.demo.moviehub.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.moviehub.data.model.Movie
import com.demo.moviehub.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadTrendingMovies()
        loadPopularMovies()
    }

    fun loadTrendingMovies(page: Int = 1) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            repository.getTrendingMovies(page = page).onSuccess { response ->
                _uiState.update { state ->
                    state.copy(
                        trendingMovies = response.results,
                        isLoading = false,
                        error = null
                    )
                }
            }.onFailure { exception ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun loadPopularMovies(page: Int = 1) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            repository.getPopularMovies(page = page).onSuccess { response ->
                _uiState.update { state ->
                    state.copy(
                        popularMovies = response.results,
                        isLoading = false,
                        error = null
                    )
                }
            }.onFailure { exception ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error"
                    )
                }
            }
        }
    }
}

data class HomeUiState(
    val trendingMovies: List<Movie> = emptyList(),
    val popularMovies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

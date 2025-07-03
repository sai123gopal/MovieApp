package com.demo.moviehub.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.moviehub.data.model.Movie
import com.demo.moviehub.data.repository.FavoriteRepository
import com.demo.moviehub.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val trendingMovies: List<Movie> = emptyList(),
    val popularMovies: List<Movie> = emptyList(),
    val favoriteMovieIds: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = combine(
        _uiState,
        favoriteRepository.getFavoriteMovies().map { favorites ->
            favorites.map { it.id }.toSet()
        }
    ) { state, favoriteIds ->
        state.copy(favoriteMovieIds = favoriteIds)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _uiState.value
    )

    init {
        loadTrendingMovies()
        loadPopularMovies()
    }


    fun loadTrendingMovies(
        timeWindow: String = "day",
        page: Int = 1
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            repository.getTrendingMovies(timeWindow, page).onSuccess { response ->
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
                        error = exception.message ?: "Failed to load trending movies"
                    )
                }
            }
        }
    }

    fun loadPopularMovies(
        page: Int = 1,
        fromDate: String? = null,
        toDate: String? = null
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            repository.getPopularMovies(
                page = page,
                fromDate = fromDate,
                toDate = toDate
            ).onSuccess { response ->
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
                        error = exception.message ?: "Failed to load popular movies"
                    )
                }
            }
        }
    }
    
    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            val isFavorite = favoriteRepository.isFavorite(movie.id)
            if (isFavorite) {
                favoriteRepository.removeFromFavorites(movie)
            } else {
                favoriteRepository.addToFavorites(movie)
            }
        }
    }
}

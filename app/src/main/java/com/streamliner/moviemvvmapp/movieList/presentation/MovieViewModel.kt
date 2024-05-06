package com.streamliner.moviemvvmapp.movieList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streamliner.moviemvvmapp.movieList.domain.repository.MovieRepository
import com.streamliner.moviemvvmapp.movieList.util.Category
import com.streamliner.moviemvvmapp.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor (
    private val movieRepository: MovieRepository
): ViewModel() {
    private var _moviesState = MutableStateFlow(MovieState())
    val moviesState = _moviesState.asStateFlow()

    init {
        getPopularMovies(false)
        getUpcomingMovies(false)
    }

    fun onEvent(event: MovieEvent) {
        when(event) {
            is MovieEvent.Navigate -> {
                _moviesState.update {
                    it.copy(
                        isCurrentPagePopular = !moviesState.value.isCurrentPagePopular
                    )
                }
            }
            is MovieEvent.Paginate -> {
                if (event.category == Category.POPULAR) {
                    getPopularMovies(forceFetchFromRemote = true)
                } else if (event.category == Category.UPCOMING) {
                    getUpcomingMovies(forceFetchFromRemote = true)
                }
            }
        }
    }

    private fun getPopularMovies(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _moviesState.update {
                it.copy(isLoading = true)
            }
            movieRepository.getMovieList(
                forceFetchFromRemote,
                Category.POPULAR,
                moviesState.value.popularMoviePage
            ).collectLatest { result ->
                when(result) {
                    is Resource.Error -> {
                        _moviesState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Success -> {
                        _moviesState.update {
                            it.copy(isLoading = false)
                        }
                        result.data?.let { popularMovies ->
                            _moviesState.update {
                                it.copy(
                                    popularMovies = moviesState.value.popularMovies + popularMovies.shuffled(),
                                    popularMoviePage = moviesState.value.popularMoviePage + 1
                                )
                            }
                        }
                    }
                    is Resource.Loading -> {
                        _moviesState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }

    private fun getUpcomingMovies(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _moviesState.update {
                it.copy(isLoading = true)
            }
            movieRepository.getMovieList(
                forceFetchFromRemote,
                Category.UPCOMING,
                moviesState.value.upcomingMoviePage
            ).collectLatest { result ->
                when(result) {
                    is Resource.Error -> {
                        _moviesState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Success -> {
                        _moviesState.update {
                            it.copy(isLoading = false)
                        }
                        result.data?.let {  upcomingMovies ->
                            _moviesState.update {
                                it.copy(
                                    upcomingMovies = moviesState.value.upcomingMovies + upcomingMovies.shuffled(),
                                    upcomingMoviePage = moviesState.value.upcomingMoviePage
                                )
                            }
                        }
                    }
                    is Resource.Loading -> {
                        _moviesState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }
}
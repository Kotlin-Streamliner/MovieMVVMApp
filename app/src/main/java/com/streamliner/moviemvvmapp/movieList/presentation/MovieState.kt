package com.streamliner.moviemvvmapp.movieList.presentation

import com.streamliner.moviemvvmapp.movieList.domain.models.Movie

data class MovieState(
    val isLoading : Boolean = false,
    val popularMoviePage : Int = 1,
    val upcomingMoviePage : Int = 1,

    val isCurrentPagePopular : Boolean = true,
    val popularMovies: List<Movie> = emptyList(),
    val upcomingMovies: List<Movie> = emptyList()
)
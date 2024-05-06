package com.streamliner.moviemvvmapp.movieList.presentation

sealed interface MovieEvent {
    data class Paginate(val category: String): MovieEvent
    object Navigate: MovieEvent
}
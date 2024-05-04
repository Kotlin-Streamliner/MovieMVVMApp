package com.streamliner.moviemvvmapp.movieList.data.remote.models

import com.google.gson.annotations.SerializedName

data class MovieList(
    val page: Int,
    val results: List<MovieDTO>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)
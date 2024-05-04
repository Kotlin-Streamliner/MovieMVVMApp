package com.streamliner.moviemvvmapp.movieList.domain.repository

import com.streamliner.moviemvvmapp.movieList.domain.models.Movie
import com.streamliner.moviemvvmapp.movieList.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovie(
        id: Int
    ): Flow<Resource<Movie>>
}
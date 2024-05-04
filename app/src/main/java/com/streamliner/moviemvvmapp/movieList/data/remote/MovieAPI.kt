package com.streamliner.moviemvvmapp.movieList.data.remote

import com.streamliner.moviemvvmapp.movieList.data.remote.models.MovieList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "9d0a29ce787ec703b2793a1ae1466d2b"
    }

    @GET("movie/{category}")
    suspend fun getMoviesList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ) : MovieList
}
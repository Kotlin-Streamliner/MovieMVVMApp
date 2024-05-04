package com.streamliner.moviemvvmapp.movieList.data.mappers

import com.streamliner.moviemvvmapp.movieList.data.local.movie.MovieEntity
import com.streamliner.moviemvvmapp.movieList.data.remote.models.MovieDTO
import com.streamliner.moviemvvmapp.movieList.domain.models.Movie

fun MovieDTO.toMovieEntity(
    category: String
): MovieEntity {
    return MovieEntity(
        adult = adult ?: false,
        backdropPath = backdropPath ?: "",
        originalLanguage = originalLanguage ?: "",
        overview = overview ?: "",
        posterPath = posterPath ?: "",
        releaseDate = releaseDate ?: "",
        title = title ?: "",
        voteAverage = voteAverage ?: 0.0,
        popularity = popularity ?: 0.0,
        voteCount = voteCount ?: 0,
        id = id ?: -1,
        originalTitle = originalTitle ?: "",
        video = video ?: false,

        category = category,

        genreIds = try {
            genreIds?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        }
    )
}

fun MovieEntity.toMovie(
    category: String
): Movie {
    return Movie(
        id = id,
        category = category,
        backdropPath = backdropPath,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        video = video,
        adult = adult,
        genreIds = try {
            genreIds.split(",").map { it.toInt() }
        } catch (e: Exception) {
            listOf(-1, -2)
        }
    )
}
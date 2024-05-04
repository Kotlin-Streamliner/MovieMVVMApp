package com.streamliner.moviemvvmapp.movieList.data.repository

import com.streamliner.moviemvvmapp.movieList.data.local.movie.MovieDatabase
import com.streamliner.moviemvvmapp.movieList.data.mappers.toMovie
import com.streamliner.moviemvvmapp.movieList.data.mappers.toMovieEntity
import com.streamliner.moviemvvmapp.movieList.data.remote.MovieAPI
import com.streamliner.moviemvvmapp.movieList.domain.models.Movie
import com.streamliner.moviemvvmapp.movieList.domain.repository.MovieRepository
import com.streamliner.moviemvvmapp.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieAPI: MovieAPI,
    private val movieDatabase: MovieDatabase
): MovieRepository {
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val localMovieList = movieDatabase.movieDAO.getMovieListByCategory(category)

            val shouldLoadLocal = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocal) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity -> movieEntity.toMovie(category) }
                ))
                emit(Resource.Loading(false))
                return@flow
            }

            val movieListFromApi = try {
                movieAPI.getMoviesList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "IOException"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "HttpException"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Exception"))
                return@flow
            }

            val movieEntities = movieListFromApi.results.let {
                it.map { movieDTO -> movieDTO.toMovieEntity(category) }
            }

            movieDatabase.movieDAO.upsertMovieList(movieEntities)
            emit(Resource.Success(
                movieEntities.map { it.toMovie(category) }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))

            val movieEntity = movieDatabase.movieDAO.getMovieById(id)
            if (movieEntity != null) {
                emit(Resource.Success(
                    movieEntity.toMovie(movieEntity.category)
                ))
                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error(message = "Movie not found"))
            emit(Resource.Loading(false))
        }
    }
}
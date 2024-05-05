package com.streamliner.moviemvvmapp.di

import com.streamliner.moviemvvmapp.movieList.data.repository.MovieRepositoryImpl
import com.streamliner.moviemvvmapp.movieList.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMovieRepository(
        movieRepository: MovieRepositoryImpl
    ): MovieRepository
}
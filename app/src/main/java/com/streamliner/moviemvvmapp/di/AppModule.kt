package com.streamliner.moviemvvmapp.di

import android.app.Application
import androidx.room.Room
import com.streamliner.moviemvvmapp.movieList.data.local.movie.MovieDatabase
import com.streamliner.moviemvvmapp.movieList.data.remote.MovieAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Singleton
    @Provides
    fun provideMovieApi(): MovieAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MovieAPI.BASE_URL)
            .client(client)
            .build()
            .create(MovieAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideMovieDatabase(app: Application) : MovieDatabase {
        return Room.databaseBuilder(app, MovieDatabase::class.java, "movie.db").build()
    }
}
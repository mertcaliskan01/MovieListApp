package com.mertcaliskan.movielistapp.data.remote

import com.mertcaliskan.movielistapp.data.entities.Movie
import com.mertcaliskan.movielistapp.data.entities.MovieList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(): Response<MovieList>

    @GET("movie/upcoming")
    suspend fun getUpcoming(): Response<MovieList>

    @GET("movie/popular")
    suspend fun getPopular(): Response<MovieList>
    @GET("movie/{movieId}")
    suspend fun getMovie(@Path("movieId") movieId: String): Response<Movie>
}

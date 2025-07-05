package com.mertcaliskan.movielistapp.data.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val movieService: MovieService
) : BaseDataSource() {
    suspend fun getNowPlayingMovies() = getResult { movieService.getNowPlayingMovies() }

    suspend fun getUpcoming() = getResult { movieService.getUpcoming() }
    suspend fun getPopular() = getResult { movieService.getPopular() }
    suspend fun getMovie(movieId: String) = getResult { movieService.getMovie(movieId) }
}

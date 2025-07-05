package com.mertcaliskan.movielistapp.data.repository

import com.mertcaliskan.movielistapp.data.remote.RemoteDataSource
import com.mertcaliskan.movielistapp.utils.performGetOperation
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    fun getNowPlayingMovies() = performGetOperation(
        networkCall = { remoteDataSource.getNowPlayingMovies() }
    )
    fun getUpcoming() = performGetOperation(
        networkCall = { remoteDataSource.getUpcoming() }
    )
    fun getPopular() = performGetOperation(
        networkCall = { remoteDataSource.getPopular() }
    )
    fun getMovie(movieId: String) = performGetOperation(
        networkCall = { remoteDataSource.getMovie(movieId) }
    )
}

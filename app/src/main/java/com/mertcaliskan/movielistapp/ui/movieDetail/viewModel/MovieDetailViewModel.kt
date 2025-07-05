package com.mertcaliskan.movielistapp.ui.movieDetail.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mertcaliskan.movielistapp.data.entities.Movie
import com.mertcaliskan.movielistapp.data.repository.MovieRepository
import com.mertcaliskan.movielistapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieId = MutableLiveData<String>()

    fun fetchMovieById(movieId: String) {
        _movieId.value = movieId
    }

    val getMovie: LiveData<Resource<Movie>> = Transformations.switchMap(_movieId) { movieId ->
        repository.getMovie(movieId ?: "")
    }
}
package com.mertcaliskan.movielistapp.ui.moviesPopular.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mertcaliskan.movielistapp.data.entities.MovieList
import com.mertcaliskan.movielistapp.data.repository.MovieRepository
import com.mertcaliskan.movielistapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    val popularMoviesList: LiveData<Resource<MovieList>> = repository.getPopular()

}
package com.mertcaliskan.movielistapp.ui.moviesNowPlaying.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.mertcaliskan.movielistapp.data.entities.MovieList
import com.mertcaliskan.movielistapp.data.repository.MovieRepository
import com.mertcaliskan.movielistapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MovieRepository
    ) : ViewModel() {

    private val loadTrigger = MutableLiveData(Unit)

    private val _playingMovies = loadTrigger.switchMap {
        repository.getNowPlayingMovies()
    }
    private val _upcomingMovies = loadTrigger.switchMap {
        repository.getUpcoming()
    }

    val movieList: LiveData<Resource<MovieList>> = _playingMovies
    val upcomingList: LiveData<Resource<MovieList>> = _upcomingMovies

    fun refresh() {
        loadTrigger.value = Unit
    }
}

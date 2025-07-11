package com.mertcaliskan.movielistapp.data.entities

import com.google.gson.annotations.SerializedName

data class MovieList(
    @SerializedName("results") val results: List<Movie>
)

package com.mertcaliskan.movielistapp.ui.moviesNowPlaying.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.mertcaliskan.movielistapp.BuildConfig
import com.mertcaliskan.movielistapp.R
import com.mertcaliskan.movielistapp.data.entities.Movie
import com.mertcaliskan.movielistapp.databinding.MoviesListItemBinding
import com.mertcaliskan.movielistapp.ui.moviesNowPlaying.MovieItemSelectListener
import com.mertcaliskan.movielistapp.utils.DateHelper.Companion.getYearFromDate

class MoviesAdapter(private val listener: MovieItemSelectListener) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {



    private val items = ArrayList<Movie>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: ArrayList<Movie>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding: MoviesListItemBinding =
            MoviesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.bind(items[position])

    inner class MovieViewHolder(
        private val itemBinding: MoviesListItemBinding,
        private val listener: MovieItemSelectListener
    ) :
        RecyclerView.ViewHolder(itemBinding.root),
        View.OnClickListener {

        private lateinit var movie: Movie

        init {
            itemBinding.root.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(movie: Movie) {
            this.movie = movie
            itemBinding.name.text = movie.name + " ("+getYearFromDate(movie.releaseDate).toString()+")"

            itemBinding.releaseDate.text = movie.overview

            // Set the signature to be the last modified time of the image file.
            val imageMetadata = movie.posterPath?.let { ObjectKey(it) }

            Glide.with(itemBinding.root.context)
                .load(BuildConfig.IMAGE_URL + movie.posterPath)
                .override(400, 400)
                .dontAnimate()
                .error(R.drawable.error)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .signature(imageMetadata)
                .into(itemBinding.image)
        }

        override fun onClick(v: View?) {
            listener.onClickedMovie(movie.id.toString())
        }
    }
}

package com.mertcaliskan.movielistapp.ui.moviesNowPlaying.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.mertcaliskan.movielistapp.BuildConfig
import com.mertcaliskan.movielistapp.R
import com.mertcaliskan.movielistapp.data.entities.Movie
import com.mertcaliskan.movielistapp.databinding.ImageViewPagerItemBinding
import com.mertcaliskan.movielistapp.ui.moviesNowPlaying.MovieItemSelectListener
import com.mertcaliskan.movielistapp.utils.DateHelper

class ImageViewPagerAdapter(private val imageUrlList: List<Movie>, private val listener: MovieItemSelectListener) :
    RecyclerView.Adapter<ImageViewPagerAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(
        val binding: ImageViewPagerItemBinding,
        private val listener: MovieItemSelectListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(movie: Movie) {
            val imageMetadata = movie.backdropPath?.let { ObjectKey(it) }

            binding.textTitle.text = movie.name + " (" + DateHelper.getYearFromDate(movie.releaseDate).toString()+")"
            binding.textDescription.text = movie.overview

            Glide.with(binding.root.context)
                .load(BuildConfig.IMAGE_URL+movie.backdropPath)
                .dontAnimate()
                .error(R.drawable.error)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .signature(imageMetadata)
                .into(binding.imageSlideIcon)

            binding.layout.setOnClickListener {
                listener.onClickedMovie(imageUrlList[bindingAdapterPosition].id.toString())
            }
        }

    }

    override fun getItemCount(): Int = imageUrlList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {

        val binding = ImageViewPagerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewPagerViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {

        holder.setData(imageUrlList[position])
    }

}
package com.mertcaliskan.movielistapp.ui.movieDetail.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.mertcaliskan.movielistapp.BuildConfig
import com.mertcaliskan.movielistapp.R
import com.mertcaliskan.movielistapp.data.entities.Movie
import com.mertcaliskan.movielistapp.databinding.MovieDetailFragmentBinding
import com.mertcaliskan.movielistapp.ui.movieDetail.viewModel.MovieDetailViewModel
import com.mertcaliskan.movielistapp.utils.DateHelper.Companion.formatToDDMMYYYY
import com.mertcaliskan.movielistapp.utils.DateHelper.Companion.getYearFromDate
import com.mertcaliskan.movielistapp.utils.Resource
import com.mertcaliskan.movielistapp.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private lateinit var layoutListener: ViewTreeObserver.OnGlobalLayoutListener
    private val viewModel: MovieDetailViewModel by viewModels()
    private var binding: MovieDetailFragmentBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MovieDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = arguments?.getString("movieId")

        if (movieId != null) {
            setupObservers(movieId)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnDiscoverPopularMovies.setOnClickListener {
            findNavController().navigate(R.id.action_movieDetailFragment_to_popularMoviesFragment)
        }


        // Set up the back button behavior
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    findNavController().navigateUp()
                }
            }
        })

    }

    private fun bindMovieView(movie: Movie) {
        binding.txtHeader.text = movie.name + "(" + getYearFromDate(movie.releaseDate).toString()+")"
        binding.txtContent.text = movie.overview


        binding.txtRate.text = String.format("%.${1}f", movie.voteAverage) + "/10"
        binding.txtReleaseDate.text = formatToDDMMYYYY(movie.releaseDate)

        layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            // Set the signature to be the last modified time of the image file.
            val imageMetadata = movie.posterPath?.let { ObjectKey(it) }

            Glide.with(binding.root.context)
                .load(BuildConfig.IMAGE_URL+movie.backdropPath)
                .dontAnimate()
                .error(R.drawable.error)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .signature(imageMetadata)
                .into(object : CustomTarget<Drawable>(binding.image.width, 1) {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        //binding.viewMovie.visibility = View.VISIBLE
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // clear resources
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        binding.image.setImageDrawable(resource)

                        //binding.viewMovie.visibility = View.VISIBLE

                        // Remove the listener
                        binding.image.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
                    }
                })
        }
        binding.image.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
    }

    private fun setupObservers(movieId: String) {
        viewModel.fetchMovieById(movieId)
        viewModel.getMovie.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.txtError.visibility = View.INVISIBLE
                    binding.scrollView.visibility = View.VISIBLE
                    if (it.data != null) {
                        bindMovieView(it.data)
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.txtError.visibility = View.VISIBLE
                    binding.scrollView.visibility = View.INVISIBLE
                }
                Resource.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE
            }
        }
    }
}

package com.mertcaliskan.movielistapp.ui.moviesNowPlaying.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.mertcaliskan.movielistapp.R
import com.mertcaliskan.movielistapp.data.entities.Movie
import com.mertcaliskan.movielistapp.databinding.MoviesFragmentBinding
import com.mertcaliskan.movielistapp.ui.moviesNowPlaying.MovieItemSelectListener
import com.mertcaliskan.movielistapp.ui.moviesNowPlaying.viewModel.MoviesViewModel
import com.mertcaliskan.movielistapp.ui.moviesNowPlaying.adapter.ImageViewPagerAdapter
import com.mertcaliskan.movielistapp.ui.moviesNowPlaying.adapter.MoviesAdapter
import com.mertcaliskan.movielistapp.utils.Resource
import com.mertcaliskan.movielistapp.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment(), MovieItemSelectListener {

    private var binding: MoviesFragmentBinding by autoCleared()
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var adapter: MoviesAdapter
    private lateinit var imageViewPagerAdapter: ImageViewPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MoviesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
            binding.swipeRefreshLayout.isRefreshing = false
        }

    }
    override fun onDestroy() {
        super.onDestroy()

        // unregistering the onPageChangedCallback
        binding.viewPager.unregisterOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {}
        )
    }
    private fun setUpViewPager() {

        binding.viewPager.adapter = imageViewPagerAdapter

        //set the orientation of the viewpager using ViewPager2.orientation
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //select any page you want as your starting page
        val currentPageIndex = 0
        binding.viewPager.currentItem = currentPageIndex

        // registering for page change callback
        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                }
            }
        )

        setupIndicators()
        setCurrentIndicator(0)
    }

    private fun setupIndicators() {
        binding.introSliderViewPager.removeAllViews()
        val indicators = arrayOfNulls<ImageView>(imageViewPagerAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(context)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            binding.introSliderViewPager.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {

        context?.let {
            val childCount = binding.introSliderViewPager.childCount
            for (i in 0 until childCount) {
                val imageView = binding.introSliderViewPager[i] as ImageView
                if (i == index) {
                    imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            it,
                            R. drawable. indicator_active))
                } else {
                    imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.indicator_inactive))

                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = MoviesAdapter(this)
        binding.rvMovieList.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.rvMovieList.adapter = adapter
    }




    private fun setupObservers() {
        viewModel.movieList.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.txtError.visibility = View.INVISIBLE
                    if (!it.data?.results.isNullOrEmpty()) {
                        adapter.setItems(ArrayList(it.data!!.results))
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.txtError.visibility = View.VISIBLE
                }
                Resource.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE
            }
        }

        viewModel.upcomingList.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.txtError.visibility = View.INVISIBLE

                    if (!it.data?.results.isNullOrEmpty()){
                        imageViewPagerAdapter = ImageViewPagerAdapter(ArrayList(it.data!!.results), this)
                        setUpViewPager()
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.txtError.visibility = View.VISIBLE
                }
                Resource.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE
            }
        }
    }



    override fun onClickedMovie(movieId: String) {

        findNavController().navigate(
            R.id.action_moviesFragment_to_movieDetailFragment,
            bundleOf("movieId" to movieId)
        )
    }
}



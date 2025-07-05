package com.mertcaliskan.movielistapp.ui.moviesPopular.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mertcaliskan.movielistapp.R
import com.mertcaliskan.movielistapp.databinding.PopulerMoviesFragmentBinding
import com.mertcaliskan.movielistapp.ui.moviesNowPlaying.MovieItemSelectListener
import com.mertcaliskan.movielistapp.ui.moviesNowPlaying.adapter.MoviesAdapter
import com.mertcaliskan.movielistapp.ui.moviesPopular.viewModel.PopularMoviesViewModel
import com.mertcaliskan.movielistapp.utils.Resource
import com.mertcaliskan.movielistapp.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PopularMoviesFragment : Fragment(), MovieItemSelectListener {

    private var binding: PopulerMoviesFragmentBinding by autoCleared()
    private val viewModel: PopularMoviesViewModel by viewModels()
    private lateinit var moviesAdapter : MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PopulerMoviesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()


        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_popularMoviesFragment_to_moviesFragment)
        }

        // Set up the back button behavior
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    findNavController().navigate(R.id.action_popularMoviesFragment_to_moviesFragment)
                }
            }
        })
    }

    private fun setupRecyclerView() {
        moviesAdapter = MoviesAdapter(this)
        binding.rvMovieList.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.rvMovieList.adapter = moviesAdapter
    }

    private fun setupObservers() {
        viewModel.popularMoviesList.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.txtError.visibility = View.INVISIBLE
                    if (!it.data?.results.isNullOrEmpty()) {
                        moviesAdapter.setItems(ArrayList(it.data!!.results))
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
            R.id.action_popularMoviesFragment_to_movieDetailFragment,
            bundleOf("movieId" to movieId)
        )
    }
}
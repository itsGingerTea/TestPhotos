package com.example.unsplashapp.ui.favoritescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.unsplashapp.App
import com.example.unsplashapp.R
import com.example.unsplashapp.data.models.Photo
import com.example.unsplashapp.databinding.FragmentFavoriteBinding
import com.example.unsplashapp.ui.favoritescreen.list.FavoriteAdapter
import com.example.unsplashapp.ui.mainscreen.MainActivity
import com.example.unsplashapp.ui.photolistscreen.PhotosCallback
import com.example.unsplashapp.utils.*
import javax.inject.Inject

class FavoriteFragment : Fragment() {

    @Inject
    lateinit var factory: FavoriteViewModelFactory.Factory
    private val viewModel by viewModels<FavoriteViewModel> {
        factory.create()
    }

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        FavoriteAdapter(object : PhotosCallback {

            override fun onItemClick(photo: Photo) {
                openCard(binding.root, photo)
            }

            override fun onLikeClick(like: Boolean, photo: Photo) {
                viewModel.onFavClicked(like, photo)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            (requireActivity() as? MainActivity)?.setSupportActionBar(tbFav)
            rvFav.layoutManager = GridLayoutManager(requireContext(), 2)
            rvFav.setHasFixedSize(true)
            rvFav.adapter = adapter
            tbFav.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            swipeFav.setOnRefreshListener {
                viewModel.onRefresh()
            }
        }
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openCard(view: View, photo: Photo) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToCardFragment(photo)
        Navigation.findNavController(view).navigate(action)
    }

    private fun injectDependencies() {
        App.appComponent.favoriteComponentBuilder().bindInflater(layoutInflater).build()
            .inject(this)
    }

    private fun setupViewModel() {
        viewModel.onViewCreated()
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                Error -> showError()
                is Success -> {
                    with(binding) {
                        swipeFav.isRefreshing = false
                        tvTitleErrorLoading.makeGone()
                    }
                }
                else -> {}
            }
        }
        viewModel.likeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SuccessState -> {
                    with(binding) {
                        favProgress.makeGone()
                        swipeFav.isRefreshing = false
                        rvFav.makeVisible()
                        tvTitleErrorLoading.makeGone()
                    }
                }
                ErrorState -> showError()
                else -> {}
            }
        }
        viewModel.favoriteLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                Loading -> showLoading()
                is Success<*> -> showList(state.data as List<Photo>?)
                else -> {}
            }
        }
    }

    private fun showList(list: List<Photo>?) {
        adapter.submitList(list)
        with(binding) {
            tvFound.text = getString(R.string.found_text, list?.size ?: 0)
            favProgress.makeGone()
            swipeFav.isRefreshing = false
            rvFav.makeVisible()
            tvTitleErrorLoading.makeGone()
        }
    }

    private fun showError() {
        with(binding) {
            favProgress.makeGone()
            swipeFav.isRefreshing = false
            rvFav.makeVisible()
            tvTitleErrorLoading.makeVisible()
        }
    }

    private fun showLoading() {
        with(binding) {
            favProgress.makeVisible()
            rvFav.makeGone()
            tvTitleErrorLoading.makeGone()
            tvTitleErrorLoading.makeGone()
        }
    }
}
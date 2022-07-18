package com.example.unsplashapp.ui.photolistscreen

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.unsplashapp.App
import com.example.unsplashapp.R
import com.example.unsplashapp.data.models.Photo
import com.example.unsplashapp.databinding.FragmentPhotolistBinding
import com.example.unsplashapp.ui.mainscreen.MainActivity
import com.example.unsplashapp.ui.photolistscreen.list.PhotoPagingAdapter
import com.example.unsplashapp.ui.photolistscreen.list.PhotosLoaderStateAdapter
import com.example.unsplashapp.utils.ErrorState
import com.example.unsplashapp.utils.SuccessState
import com.example.unsplashapp.utils.snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class PhotoFragment : Fragment() {

    @Inject
    lateinit var factory: PhotoViewModelFactory.Factory
    private val viewModel by viewModels<PhotoViewModel> {
        factory.create()
    }

    private var _binding: FragmentPhotolistBinding? = null
    private val binding get() = _binding!!
    private var topicId: String? = null
    private var topicTitle: String? = null
    private var topicCount: Int = 0
    private val args: PhotoFragmentArgs by navArgs()
    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        PhotoPagingAdapter(object : PhotosCallback {

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
        topicId = args.id
        topicTitle = args.title
        topicCount = args.count
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotolistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        with(binding) {
            (requireActivity() as? MainActivity)?.setSupportActionBar(photolistToolbar)
            rvCatalog.layoutManager = GridLayoutManager(requireContext(), 2)
            rvCatalog.setHasFixedSize(true)
            rvCatalog.adapter = adapter.withLoadStateHeaderAndFooter(
                header = PhotosLoaderStateAdapter { adapter.retry() },
                footer = PhotosLoaderStateAdapter { adapter.retry() }
            )
            photolistToolbar.title = getString(R.string.category, topicTitle)
            photolistToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            btnTryAgain.setOnClickListener {
                adapter.retry()
            }
            swipe.setOnRefreshListener {
                adapter.refresh()
                binding.swipe.isRefreshing = false
            }
        }
        setupViewModel(view)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photos
                    .collectLatest(adapter::submitData)
            }
        }
        adapter.addLoadStateListener { state ->
            with(binding) {
                progressBarCatalog.isVisible = state.refresh == LoadState.Loading
                rvCatalog.isVisible = state.refresh != LoadState.Loading
                errorMessage.isVisible = state.refresh is LoadState.Error
                if (errorMessage.isVisible) {
                    tvFound.text = getString(R.string.found_text, 0)
                } else {
                    tvFound.text = getString(R.string.found_text, topicCount)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.photolist_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fav_menu -> {
                openFavorites()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun injectDependencies() {
        App.appComponent.photoComponentBuilder().bindInflater(layoutInflater).build().inject(this)
    }

    private fun setupViewModel(view: View) {
        topicId?.let {
            viewModel.onViewCreated(it)
        }
        viewModel.flagShowLike.observe(viewLifecycleOwner) { flag ->
            if (flag)
                setHasOptionsMenu(true)
            else
                setHasOptionsMenu(false)
            adapter.addFlag(flag)
        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                SuccessState -> {}
                is ErrorState -> showError(view)
                else -> {}
            }
        }
        viewModel.snackbar.observe(viewLifecycleOwner) {
            snackbar(view, it)
        }
    }

    private fun openCard(view: View, photo: Photo) {
        val action = PhotoFragmentDirections.actionPhotoFragmentToCardFragment(photo)
        Navigation.findNavController(view).navigate(action)
    }

    private fun openFavorites() {
        val action = PhotoFragmentDirections.actionPhotoFragmentToFavoriteFragment()
        findNavController().navigate(action)
    }

    private fun showError(view: View) {
        snackbar(view, R.string.fav_error_loading)
    }
}

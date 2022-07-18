package com.example.unsplashapp.ui.topicsscreen

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unsplashapp.App
import com.example.unsplashapp.R
import com.example.unsplashapp.databinding.FragmentTopicsBinding
import com.example.unsplashapp.ui.mainscreen.MainActivity
import com.example.unsplashapp.ui.topicsscreen.list.TopicsAdapter
import com.example.unsplashapp.ui.topicsscreen.list.TopicsLoaderStateAdapter
import com.example.unsplashapp.utils.ErrorState
import com.example.unsplashapp.utils.snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopicsFragment : Fragment() {

    @Inject
    lateinit var factory: TopicsViewModelFactory.Factory

    private val viewModel by viewModels<TopicsViewModel> {
        factory.create()
    }
    private var _binding: FragmentTopicsBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        TopicsAdapter(
            onClick = { id, title, count ->
                val action =
                    TopicsFragmentDirections.actionTopicsFragmentToPhotoFragment(id, title, count)
                findNavController().navigate(action)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopicsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        viewModel.onCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        with(binding) {
            (requireActivity() as? MainActivity)?.setSupportActionBar(tbTopics)
            rvTopics.layoutManager = LinearLayoutManager(requireContext())
            rvTopics.setHasFixedSize(true)
            rvTopics.adapter = adapter.withLoadStateHeaderAndFooter(
                header = TopicsLoaderStateAdapter { adapter.retry() },
                footer = TopicsLoaderStateAdapter { adapter.retry() }
            )
            btnTryAgain.setOnClickListener {
                adapter.refresh()
            }
            swipeTopics.setOnRefreshListener {
                adapter.refresh()
                swipeTopics.isRefreshing = false
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.topics
                    .collectLatest(adapter::submitData)
            }
        }
        adapter.addLoadStateListener { state ->
            with(binding) {
                progressBarTopics.isVisible = state.refresh == LoadState.Loading
                rvTopics.isVisible = state.refresh != LoadState.Loading
                rvTopics.isVisible = state.refresh !is LoadState.Error
                errorMessage.isVisible = state.refresh is LoadState.Error
                tvTitleErrorLoading.isVisible = state.refresh is LoadState.Error
            }
        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ErrorState -> showError(view)
                else -> {}
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.topics_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_menu -> {
                openLogin()
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
        App.appComponent.topicsComponentBuilder().bindInflater(layoutInflater).build().inject(this)
    }

    private fun showError(view: View) {
        snackbar(view, R.string.fav_error_loading)
    }

    private fun openLogin() {
        viewModel.onLogoutClicked()
        val action = TopicsFragmentDirections.actionTopicsFragmentToLoginUnsplashFragment()
        findNavController().navigate(action)
    }
}
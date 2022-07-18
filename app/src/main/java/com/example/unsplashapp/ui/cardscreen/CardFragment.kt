package com.example.unsplashapp.ui.cardscreen

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.unsplashapp.App
import com.example.unsplashapp.R
import com.example.unsplashapp.data.models.Photo
import com.example.unsplashapp.databinding.FragmentCardBinding
import com.example.unsplashapp.utils.snackbar
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import javax.inject.Inject

class CardFragment : Fragment() {

    @Inject
    lateinit var factory: CardViewModelFactory.Factory
    private val viewModel by viewModels<CardViewModel> {
        factory.create()
    }
    private var _binding: FragmentCardBinding? = null
    private val binding get() = _binding!!
    private var photos: Photo? = null
    private val args: CardFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        viewModel.onCreate()
        photos = args.photo
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            photos?.let { photo ->
                val requestPermissionLauncher =
                    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                        if (isGranted) {
                            viewModel.onDownloadPhotoClicked(
                                false,
                                photo.urls.regular,
                                requireContext()
                            )
                        } else {
                            snackbar(view, R.string.permission_denied)
                        }
                    }
                cardFav.apply {
                    setOnCheckedChangeListener(null)
                    isChecked = photo.favorite
                    setOnCheckedChangeListener { _, b ->
                        viewModel.onFavoriteClicked(b)
                    }
                }
                setupViewModel(view, photo.id)
                Picasso.get()
                    .load(photo.urls.regular)
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .memoryPolicy(MemoryPolicy.NO_STORE)
                    .placeholder(ColorDrawable(Color.TRANSPARENT))
                    .into(imgCard)
                cardLike.text =
                    context?.getString(R.string.likes, photo.likes)
                cardAuthorUsername.text =
                    context?.getString(R.string.author_username, photo.user.username)
                if (photo.user.instagramUsername != null) {
                    cardAuthorInsta.text =
                        context?.getString(
                            R.string.author_insta_username,
                            photo.user.instagramUsername
                        )
                } else {
                    cardAuthorInsta.visibility = View.INVISIBLE
                }
                btnDownload.setOnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        viewModel.onDownloadPhotoClicked(true, photo.urls.regular, requireContext())
                    } else {
                        requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun injectDependencies() {
        App.appComponent.cardComponentBuilder().bindInflater(layoutInflater).build().inject(this)
    }

    private fun setupViewModel(view: View, id: String) {
        viewModel.onViewCreated(id)
        viewModel.loggedUserValue.observe(viewLifecycleOwner) { isLogged ->
            binding.cardFav.isVisible = isLogged
        }
        viewModel.snackbar.observe(viewLifecycleOwner) { n ->
            this.snackbar(view, n)
        }
        viewModel.favorite.observe(viewLifecycleOwner) { isFavorite ->
            binding.cardFav.isChecked = isFavorite
        }
    }
}
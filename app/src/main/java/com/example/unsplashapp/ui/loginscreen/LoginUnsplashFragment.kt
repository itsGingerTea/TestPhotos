package com.example.unsplashapp.ui.loginscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.unsplashapp.databinding.FragmentLoginUnsplashBinding

class LoginUnsplashFragment : Fragment() {
    private var _binding: FragmentLoginUnsplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginUnsplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            buttonLogin.setOnClickListener {
                val action = LoginUnsplashFragmentDirections.actionLoginScreenToAuthScreen()
                findNavController().navigate(action)
            }
            textGuest.setOnClickListener {
                val action =
                    LoginUnsplashFragmentDirections.actionLoginUnsplashFragmentToTopicsFragment()
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
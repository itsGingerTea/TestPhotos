package com.example.unsplashapp.ui.mainscreen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.unsplashapp.App
import com.example.unsplashapp.R
import com.example.unsplashapp.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: MainActivityViewModelFactory.Factory
    private val viewModel by viewModels<MainActivityViewModel> {
        factory.create()
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        injectDependencies()
        setupViewModel()
    }

    private fun setupViewModel() {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navGraph = navHostFragment.navController.navInflater.inflate(R.navigation.nav_graph)
        val navController = navHostFragment.navController
        viewModel.onCreate()
        viewModel.loginResult.observe(this) { result ->
            val destination = if (result)
                R.id.topicsFragment
            else
                R.id.loginUnsplashFragment
            navGraph.setStartDestination(destination)
            navController.graph = navGraph
        }
    }

    private fun injectDependencies() {
        App.appComponent.mainComponentBuilder().bindInflater(layoutInflater).build().inject(this)
    }
}
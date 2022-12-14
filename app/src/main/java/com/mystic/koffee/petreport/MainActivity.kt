package com.mystic.koffee.petreport

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.mystic.koffee.petreport.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_container)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setup() {
        actionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.primaryColor)))
        setupNavHost()
    }

    private fun setupNavHost() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navHost.navController.apply {
            val navGraph = navInflater.inflate(R.navigation.nav_graph)
            graph = navGraph
        }
    }
}
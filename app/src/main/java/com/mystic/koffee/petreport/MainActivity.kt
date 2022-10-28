package com.mystic.koffee.petreport

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.mystic.koffee.petreport.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
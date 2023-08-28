package com.example.workv1

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.workv1.databinding.ActivityMainBinding
import com.example.workv1.health.HealthDataManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import androidx.appcompat.app.ActionBar


class MainActivity : AppCompatActivity() {

private lateinit var binding: ActivityMainBinding

    var healthDataManager: HealthDataManager  = HealthDataManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityMainBinding.inflate(layoutInflater)

            setContentView(binding.root)


            val navView: BottomNavigationView = binding.navView
            setSupportActionBar(binding.homeToolbar)
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
//        setSupportActionBar(binding.homeToolbar)
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_wallet,
                    R.id.navigation_notifications,
                    R.id.navigation_profile
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)


        } catch (e: Exception) {
                System.out.println(e.message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}
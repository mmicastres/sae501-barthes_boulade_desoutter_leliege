package com.example.hiker.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hiker.managers.UserLevelManager
import com.example.hiker.services.LocationService
import com.example.hiker.ui.components.BottomNavigationBar
import com.example.hiker.ui.components.ConnectionPage
import com.example.hiker.ui.components.HikersPage
import com.example.hiker.ui.components.MapPage
import com.example.hiker.ui.components.ProfilePage

class MainActivity : ComponentActivity() {
    private lateinit var locationService: LocationService
    private val userLevelManager = UserLevelManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationService = LocationService(this, lifecycleScope)

        setContent {
            val navController = rememberNavController()
            val currentRoute = remember { mutableStateOf<String?>(null) }

            LaunchedEffect(navController) {
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    currentRoute.value = destination.route
                }
            }

            Scaffold(
                bottomBar = {
                    if (shouldShowBottomBar(currentRoute.value)) {
                        BottomNavigationBar(navController)
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "Connection",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("hikers") { HikersPage() }
                    composable("map") { MapPage(locationService) }
                    composable("profile") { ProfilePage(locationService, userLevelManager) }
                    composable("connection") { ConnectionPage(navController) }
                }
            }
        }
    }
}

fun shouldShowBottomBar(route: String?): Boolean {
    return route != "connection"
}
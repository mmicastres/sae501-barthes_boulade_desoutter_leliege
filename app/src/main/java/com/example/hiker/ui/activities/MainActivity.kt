package com.example.hiker.ui.activities

import HikersViewModel
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hiker.managers.UserLevelManager
import com.example.hiker.services.LocationService
import com.example.hiker.ui.components.BottomNavigationBar
import com.example.hiker.ui.components.ConnectionPage
import com.example.hiker.ui.components.HikersPage
import com.example.hiker.ui.components.InscriptionPage
import com.example.hiker.ui.components.MapPage
import com.example.hiker.ui.components.ProfilePage
import com.example.hiker.ui.components.WelcomePage
import org.osmdroid.config.Configuration


class MainActivity : ComponentActivity() {
    private lateinit var locationService: LocationService
    private val userLevelManager = UserLevelManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationService = LocationService(this, lifecycleScope)
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, getSharedPreferences("osmdroid", 0))

        setContent {
            val navController = rememberNavController()
            val currentRoute = remember { mutableStateOf<String?>(null) }
            val model : HikersViewModel = viewModel()

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
                    startDestination = "WelcomePage",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("hikers") { HikersPage(model) }
                    composable("map") { MapPage(locationService, model) }
                    composable("profile") { ProfilePage(locationService, userLevelManager, navController, model) }
                    composable("connection") { ConnectionPage(navController, model) }
                    composable("inscription") { InscriptionPage(navController, model) }
                    composable("WelcomePage") { WelcomePage(navController) }
                }
            }
        }
    }
}

fun shouldShowBottomBar(route: String?): Boolean {
    return !(route == "connection" || route == "inscription" || route == "WelcomePage")
}
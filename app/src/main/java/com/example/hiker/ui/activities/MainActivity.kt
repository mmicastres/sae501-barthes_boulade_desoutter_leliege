package com.example.hiker.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hiker.ui.components.HikersPage
import com.example.hiker.ui.components.MapPage
import com.example.hiker.ui.components.ProfilePage
import com.example.hiker.R
import com.example.hiker.managers.UserLevelManager
import com.example.hiker.services.LocationService

class MainActivity : ComponentActivity() {
    private lateinit var locationService: LocationService
    private val userLevelManager = UserLevelManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationService = LocationService(this, lifecycleScope)

        setContent {
            val navController = rememberNavController()

            Scaffold(
                bottomBar = { BottomNavigationBar(navController) }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "profile",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("hikers") { HikersPage() }
                    composable("map") { MapPage() }
                    composable("profile") { ProfilePage(locationService, userLevelManager) }
                }
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(backgroundColor = Color.White, contentColor = Color.Black) {
        BottomNavigationItem(
            icon = { Icon(painter = painterResource(id = R.drawable.icon_cartes), contentDescription = null) },
            label = { Text("Hikers") },
            selected = navController.currentDestination?.route == "hikers",
            onClick = { navController.navigate("hikers") }
        )
        BottomNavigationItem(
            icon = { Icon(painter = painterResource(id = R.drawable.icon_map), contentDescription = null) },
            label = { Text("Map") },
            selected = navController.currentDestination?.route == "map",
            onClick = { navController.navigate("map") }
        )
        BottomNavigationItem(
            icon = { Icon(painter = painterResource(id = R.drawable.icon_profile), contentDescription = null) },
            label = { Text("Profile") },
            selected = navController.currentDestination?.route == "profile",
            onClick = { navController.navigate("profile") }
        )
    }
}

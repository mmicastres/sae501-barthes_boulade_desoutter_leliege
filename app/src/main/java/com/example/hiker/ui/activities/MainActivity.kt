package com.example.hiker.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.hiker.services.LocationService
import com.example.hiker.managers.UserLevelManager
import com.example.hiker.ui.components.StatsPage

class MainActivity : ComponentActivity() {
    private lateinit var locationService: LocationService
    private val userLevelManager = UserLevelManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationService = LocationService(this, lifecycleScope)

        setContent {
            StatsPage(locationService, userLevelManager)
        }
    }
}

package com.example.hiker.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.hiker.services.LocationService
import com.example.hiker.ui.components.DisplayLocation
import com.example.hiker.managers.UserLevelManager

class MainActivity : ComponentActivity() {
    private lateinit var locationService: LocationService
    private val userLevelManager = UserLevelManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationService = LocationService(this, lifecycleScope)

        setContent {
            var isLocationAvailable by remember { mutableStateOf(false) }

            // Souscrire aux mises à jour de localisation
            DisposableEffect(locationService) {
                val observer = object : LocationService.LocationUpdateObserver {
                    override fun onLocationUpdated() {
                        isLocationAvailable = locationService.isLocationAvailable()
                    }
                }
                locationService.addObserver(observer)
                onDispose {
                    locationService.removeObserver(observer)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { locationService.toggleLocationUpdates() }) {
                    Text(text = if (isLocationAvailable) "Stop Location Updates" else "Start Location Updates")
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (isLocationAvailable) {
                    DisplayLocation(locationService, userLevelManager)
                }
            }
        }
    }
}

package com.example.hiker

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.hiker.model.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

private var lon by mutableStateOf<Double?>(null)
private var lat by mutableStateOf<Double?>(null)


class MainActivity : ComponentActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocations by mutableStateOf(listOf<Location>())
    private var lastLocations by mutableStateOf(listOf<Location>())
    private var isLocationUpdatesEnabled by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { toggleLocationUpdates() }) {
                    Text(text = if (isLocationUpdatesEnabled) "Stop Location Updates" else "Start Location Updates")
                }
                Spacer(modifier = Modifier.height(16.dp))
                DisplayLocation("Current Location", currentLocations)
                Spacer(modifier = Modifier.height(16.dp))
                DisplayLocation("Last Location", lastLocations)
            }
        }
    }

    @Composable
    fun DisplayLocation(title: String, locations: List<Location>) {
        Column {
            Text(text = "$title:")
            locations.forEachIndexed { index, location ->
                Text(text = "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
            }
        }
    }

    private fun toggleLocationUpdates() {
        lifecycleScope.launch {
            if (isLocationUpdatesEnabled) {
                stopLocationUpdates()
            } else {
                startLocationUpdates()
            }
            isLocationUpdatesEnabled = !isLocationUpdatesEnabled
        }
    }

    private fun startLocationUpdates() {
        lifecycleScope.launch {
            while (isLocationUpdatesEnabled) {
                fetchLocation()
                delay(5000) // Attendez 5 secondes avant de rafraîchir la position
            }
        }
    }

    private fun stopLocationUpdates() {
        // Arrêtez les mises à jour de la position ici si nécessaire
    }

    private fun fetchLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : com.google.android.gms.location.LocationCallback() {
                override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                    val lastLocation = locationResult.lastLocation
                    lon = lastLocation?.longitude
                    lat = lastLocation?.latitude

                    // Mettez à jour les emplacements ici
                    updateLocations(Location(lat ?: 0.0, lon ?: 0.0))
                }
            },
            null
        )
    }

    private fun updateLocations(location: Location) {
        lifecycleScope.launch(Dispatchers.Main) {
            // Mettez à jour lastLocations avec les coordonnées actuelles
            lastLocations = ArrayList(currentLocations)

            // Mettez à jour currentLocations avec les nouvelles coordonnées
            currentLocations = listOf(location)
        }
    }
}

package com.example.hiker.services


import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.hiker.utils.DistanceCalculator
import com.google.android.gms.location.*
import kotlinx.coroutines.launch

class LocationService(private val context: Context, private val lifecycleScope: LifecycleCoroutineScope) {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var totalDistance: Float = 0f
    var lon by mutableStateOf<Double?>(null)
    var lat by mutableStateOf<Double?>(null)
    interface LocationUpdateObserver {
        fun onLocationUpdated()
    }

    private val observers = mutableListOf<LocationUpdateObserver>()

    fun addObserver(observer: LocationUpdateObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: LocationUpdateObserver) {
        observers.remove(observer)
    }

    private fun notifyObservers() {
        observers.forEach { it.onLocationUpdated() }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation = locationResult.lastLocation
            val newLat = lastLocation?.latitude
            val newLon = lastLocation?.longitude

            if (lat != null && lon != null && newLat != null && newLon != null) {
                val distance = DistanceCalculator.calculateDistance(lat!!, lon!!, newLat, newLon)
                totalDistance += distance
            }

            lat = newLat
            lon = newLon
            notifyObservers()
        }
    }

    init {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }

    fun toggleLocationUpdates() {
        lifecycleScope.launch {
            if (lon != null && lat != null) {
                stopLocationUpdates()
            } else {
                if (checkLocationPermission()) {
                    startLocationUpdates()
                } else {
                    requestLocationPermission()
                }
            }
        }
    }

    private fun startLocationUpdates() {
        lifecycleScope.launch {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).apply {
                setMinUpdateDistanceMeters(5F)
                setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                setWaitForAccurateLocation(true)
            }.build()

            try {
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
            } catch (e: SecurityException) {
                // Gérer l'exception de sécurité ici
            }
        }
    }

    private fun stopLocationUpdates() {
        lifecycleScope.launch {
            try {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            } catch (e: SecurityException) {
                // Gérer l'exception de sécurité ici
            }
            lon = null
            lat = null
            notifyObservers()
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        if (context is Activity) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // Gérer l'erreur ou lancer une exception
        }
    }

    fun isLocationAvailable(): Boolean {
        return lon != null && lat != null
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 101
    }
}

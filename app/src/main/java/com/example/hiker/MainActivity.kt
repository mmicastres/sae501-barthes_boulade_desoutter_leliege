package com.example.hiker

import android.os.Build
import android.os.Bundle
import android.Manifest
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.hiker.ui.theme.HikerTheme
import com.google.android.gms.location.*
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var totalDistance: Float = 0f
    private var lon by mutableStateOf<Double?>(null)
    private var lat by mutableStateOf<Double?>(null)
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation = locationResult.lastLocation
            val newLat = lastLocation?.latitude
            val newLon = lastLocation?.longitude

            if (lat != null && lon != null && newLat != null && newLon != null) {
                val distance = calculateDistance(lat!!, lon!!, newLat, newLon)
                totalDistance += distance
            }

            lat = newLat
            lon = newLon
        }
    }


    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            HikerTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = { toggleLocationUpdates() }) {
                        Text(text = if (lon != null && lat != null) "Stop Location Updates" else "Start Location Updates")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (lon != null && lat != null) {
                        DisplayLocation("Current Location")
                    }
                }
            }
        }
    }



    @Composable
    fun DisplayLocation(title: String) {
        Column {
            Text(text = "$title:")
            Text(text = "Latitude: ${lat ?: 0.0}, Longitude: ${lon ?: 0.0}")
            Text(text = "Distance parcourue: $totalDistance mètres")
            Niveaux(totalDistance = totalDistance)
        }
    }


    @Composable
    fun Niveaux(totalDistance: Float) {
        // Utilisez remember pour sauvegarder l'état même lorsque la composition est réexécutée
        remember { mutableStateOf(ClassKil()) }

        // Mise à jour de l'état lorsque l'utilisateur parcourt 10 kilomètres
        Column {
            Text(text = "Niveau actuel: ${calculateLevel(totalDistance)}")
            Text(text = "Grade: ${getGrade(calculateLevel(totalDistance))}")
        }
    }

    private fun calculateLevel(totalDistance: Float): Int {
        // Changer la logique selon les besoins

        return (totalDistance / 5000).toInt() + 1
    }

    data class ClassKil(
        val niveau: Int = 1,
        val kilometres: Int = 0,
        val grade: String = "Apprenti randonneur"
    )

    private fun getGrade(niveau: Int): String {
        return when (niveau) {
            in 1..4 -> "Apprenti randonneur"
            in 5..9 -> "Randonneur occasionnel"
            in 10..14 -> "Randonneur"
            in 15..19 -> "Randonneur expérimenté"
            in 20..24 -> "Maître randonneur"
            in 25..29 -> "Grand maître randonneur"
            in 30..34 -> "Randonneur suprême"
            else -> "Éminent randonneur de l'ordre des Hikers"
        }
    }

    private fun toggleLocationUpdates() {
        lifecycleScope.launch {
            if (lon != null && lat != null) {
                stopLocationUpdates()
            } else {
                // Vérifiez d'abord l'autorisation
                if (checkLocationPermission()) {
                    startLocationUpdates()
                } else {
                    // Si l'autorisation n'est pas accordée, demandez-la
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
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }


    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val earthRadius = 6371000.0 // Rayon de la Terre en mètres

        // Convertir les latitudes et longitudes en radians
        val lat1Rad = Math.toRadians(lat1)
        val lon1Rad = Math.toRadians(lon1)
        val lat2Rad = Math.toRadians(lat2)
        val lon2Rad = Math.toRadians(lon2)

        // Calculer la distance entre les deux points en utilisant la formule de la loi des cosinus
        val dLon = lon2Rad - lon1Rad
        val dLat = lat2Rad - lat1Rad
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(lat1Rad) * cos(lat2Rad) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return (earthRadius * c).toFloat()
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 101
    }
}

    package com.example.hiker.ui.components

    import android.util.Log
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.height
    import androidx.compose.material3.Text
    import androidx.compose.ui.Modifier
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.unit.dp
    import com.example.hiker.services.LocationService
    import com.example.hiker.managers.UserLevelManager

    @Composable
    fun DisplayLocation(locationService: LocationService, userLevelManager: UserLevelManager) {
        val lat = locationService.lat
        val lon = locationService.lon
        val totalDistance = locationService.totalDistance

        Column {
            Text(text = "Current Location:")
            Log.d("LocationService", "New lat: $lat, New lon: $lon")
            Text(text = "Latitude: ${lat ?: 0.0}, Longitude: ${lon ?: 0.0}")
            Text(text = "Distance parcourue: $totalDistance mètres")
            Spacer(modifier = Modifier.height(16.dp))
            DisplayNiveaux(totalDistance, userLevelManager)
        }
    }

    @Composable
    fun DisplayNiveaux(totalDistance: Float, userLevelManager: UserLevelManager) {
        val level = userLevelManager.calculateLevel(totalDistance)
        val grade = userLevelManager.getGrade(level)

        Column {
            Text(text = "Niveau actuel: $level")
            Text(text = "Grade: $grade")
        }
    }

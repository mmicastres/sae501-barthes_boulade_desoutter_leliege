package com.example.hiker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hiker.services.LocationService
import com.example.hiker.managers.UserLevelManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun ProfilePage(locationService: LocationService, userLevelManager: UserLevelManager) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Nom d'utilisateur: User",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            DisplayNiveaux(locationService.totalDistance, userLevelManager)
            Spacer(modifier = Modifier.height(16.dp))
            GridStatSection(locationService)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            UniversalButton(
                onClickAction = { locationService.toggleLocationUpdates() },
                buttonText = if (locationService.isLocationAvailable()) "Stop Location Updates" else "Start Location Updates"
            )
        }
    }
}


@Composable
fun GridStatSection(locationService: LocationService) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            val formattedLat = "%.2f".format(locationService.lat ?: 0.0)
            val formattedLon = "%.2f".format(locationService.lon ?: 0.0)
            StatBubble(title = "Position Actuelle", content = "$formattedLat, $formattedLon")
            StatBubble(
                title = "Distance Totale",
                content = "${locationService.totalDistance} mètres"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            StatBubble(title = "Duels Gagnés", content = "À venir")
            StatBubble(title = "Collection", content = "À venir")
        }
    }
}

@Composable
fun StatBubble(title: String, content: String) {
    Box(
        modifier = Modifier
            .size(width = 150.dp, height = 150.dp)
            .background(color = Color.LightGray, shape = RoundedCornerShape(75.dp)),
        contentAlignment = Alignment.Center  // Aligner le contenu au centre
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 4.dp)  // Ajuster la marge interne
            )
            Text(text = content)
        }
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

@Composable
fun UniversalButton(onClickAction: () -> Unit, buttonText: String) {
    Button(onClick = onClickAction) {
        Text(text = buttonText)
    }
}

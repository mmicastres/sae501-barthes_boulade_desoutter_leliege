package com.example.hiker.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hiker.R
import com.example.hiker.ui.theme.Jaune
import com.example.hiker.ui.theme.Noir

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = Jaune,
        contentColor = Color.Black,
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) // Arrondit les coins en haut
    ) {
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_cartes),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp) // Définissez la taille de l'icône ici
                )
            },
            label = { Text("Hikers", color = Noir) },
            selected = navController.currentDestination?.route == "hikers",
            onClick = { navController.navigate("hikers") }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_map),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp) // Et ici
                )
            },
            label = { Text("Map", color = Noir) },
            selected = navController.currentDestination?.route == "map",
            onClick = { navController.navigate("map") }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_profile),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp) // Et aussi ici
                )
            },
            label = { Text("Profile", color = Noir) },
            selected = navController.currentDestination?.route == "profile",
            onClick = { navController.navigate("profile") }
        )
    }
}
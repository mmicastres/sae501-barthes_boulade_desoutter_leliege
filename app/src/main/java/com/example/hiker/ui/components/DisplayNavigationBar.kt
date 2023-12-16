package com.example.hiker.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        contentColor = Color.Black
    ) {
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_cartes),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
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
                    modifier = Modifier.size(26.dp)
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
                    modifier = Modifier.size(26.dp)
                )
            },
            label = { Text("Profile", color = Noir) },
            selected = navController.currentDestination?.route == "profile",
            onClick = { navController.navigate("profile") }
        )
    }
}
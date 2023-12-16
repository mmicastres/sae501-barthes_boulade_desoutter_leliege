package com.example.hiker.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.hiker.R
import com.example.hiker.ui.theme.Jaune

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(backgroundColor = Jaune, contentColor = Color.Black) {
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

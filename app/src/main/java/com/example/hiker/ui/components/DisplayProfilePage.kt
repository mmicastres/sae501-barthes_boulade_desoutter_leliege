package com.example.hiker.ui.components

import HikersViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hiker.R
import com.example.hiker.managers.UserLevelManager
import com.example.hiker.services.LocationService
import com.example.hiker.ui.theme.Maron
import com.example.hiker.ui.theme.Jaune
import java.util.Locale


@Composable
fun ProfilePage(locationService: LocationService, userLevelManager: UserLevelManager, navController: NavController, viewModel: HikersViewModel) {
    val userInfo = viewModel.userInfo.collectAsState().value
    val backgroundImage: Painter = painterResource(id = R.drawable.backgroud_image)
    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp, 16.dp, 20.dp, 0.dp)
            .verticalScroll(scrollState)
    ) {
        userInfo?.let {
            DisplayNiveaux(it.totalKmParcourus.toFloat(), userLevelManager, viewModel)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = backgroundImage,
                contentDescription = "Image de fond",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.7f)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                GridStatSection(locationService, viewModel)
                Spacer(modifier = Modifier.height(24.dp)) // Espace supplémentaire pour le défilement
                Parametres()
                Boutons(locationService, navController, viewModel)
            }
        }
    }
}
@Composable
fun GridStatSection(locationService: LocationService, viewModel: HikersViewModel) {
    val userInfo = viewModel.userInfo.collectAsState().value
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {

            val distancetotale = (userInfo?.totalKmParcourus)?.plus(locationService.totalDistance)

            userInfo?.let {
                StatBubble(
                    title = "Distance totale",
                    content = "${distancetotale}  mètres"
                )
            }
            userInfo?.let {
                StatBubble(
                    title = "Distance actuelle",
                    content = "${locationService.totalDistance} mètres"
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            userInfo?.let {
                StatBubble(title = "Duels Gagnés", content = "${userInfo.duelsGagnes}")
            }
            val personnagesContent = if (userInfo?.personnagesObtenus.isNullOrEmpty()) {
                "0/6"
            } else {
                "${userInfo?.personnagesObtenus?.size}/6"
            }
            StatBubble(title = "Collection", content = personnagesContent)
        }
    }
}

@Composable
fun StatBubble(title: String, content: String) {
    Box(
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(Maron),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title.uppercase(Locale.ROOT),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = content,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.White
                )
            )
        }
    }
}

@Composable
fun DisplayNiveaux(totalDistance: Float, userLevelManager: UserLevelManager, viewModel: HikersViewModel) {
    val level = userLevelManager.calculateLevel(totalDistance)
    val grade = userLevelManager.getGrade(level)
    val userInfo = viewModel.userInfo.collectAsState().value

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Louna la rando du 81",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                color = Color.Black
            ),
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nv. $level",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                ),
            )
            Text(
                text = grade,
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                ),
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}

@Composable
fun Parametres() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Paramètres", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Musique")
                Switch(
                    checked = true,
                    onCheckedChange = { /* Gérer le changement */ },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Jaune,
                        checkedTrackColor = Jaune.copy(alpha = 0.5f)
                    )
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Son")
                Switch(
                    checked = true,
                    onCheckedChange = { /* Gérer le changement */ },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Jaune,
                        checkedTrackColor = Jaune.copy(alpha = 0.5f)
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Langue : Français")
        Divider(color = Color.Black, thickness = 1.dp)
    }
}
@Composable
fun Boutons(locationService: LocationService, navController: NavController, viewModel: HikersViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { /* Gérer l'aide */ }) {
            Text(text = "Aide")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.logout()
                navController.navigate("connection")
            }
        ) {
            Text(text = "Se déconnecter", color = Color.White)
        }
        UniversalButton(
            onClickAction = { locationService.toggleLocationUpdates() },
            buttonText = if (locationService.isLocationAvailable()) "Stop Location Updates" else "Start Location Updates"
        )
    }
}

@Composable
fun UniversalButton(onClickAction: () -> Unit, buttonText: String, modifier: Modifier = Modifier) {
    Button(
        onClick = onClickAction,
        modifier = modifier
    ) {
        Text(text = buttonText)
    }
}
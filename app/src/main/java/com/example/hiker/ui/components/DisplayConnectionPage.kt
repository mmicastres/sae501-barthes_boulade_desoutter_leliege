package com.example.hiker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hiker.R
import com.example.hiker.ui.theme.Beige
import com.example.hiker.ui.theme.Maron

@Composable
fun ConnectionPage(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val background = painterResource(id = R.drawable.background)

    Box(modifier = Modifier.fillMaxHeight()) {
        Image(
            painter = background,
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Connexion",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp,
                color = Color.Black
            ),
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp)
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Beige)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StyledTextField(
                value = username,
                onValueChange = { username = it },
                label = "Login"
            )

            Spacer(modifier = Modifier.height(8.dp))

            StyledTextField(
                value = password,
                onValueChange = { password = it },
                label = "Mot de passe"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(navController)
        }
    }
}

@Composable
fun StyledTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .border(2.dp, Color.Black, RoundedCornerShape(4.dp))
                    .background(Color(0xFFFFF0D9), RoundedCornerShape(4.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    Modifier
                        .padding(start = 4.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (value.isEmpty()) {
                        Text(label, color = Color.Gray, style = MaterialTheme.typography.body1)
                    }
                    innerTextField()
                }
            }
        }
    )
}
@Composable
fun CustomButton(navController: NavController) {
    val buttonBackgroundColor = Maron
    val buttonContentColor = Color.White
    val outlinedButtonContentColor = Color.Black

    Button(
        onClick = { navController.navigate("profile") },
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonBackgroundColor, contentColor = buttonContentColor),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(text = "Se connecter", style = MaterialTheme.typography.button)
    }
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(
        onClick = { navController.navigate("inscription") },
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent, contentColor = outlinedButtonContentColor),
        border = BorderStroke(1.dp, buttonBackgroundColor),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(text = "Je n'ai pas de compte", style = MaterialTheme.typography.button)
    }
}
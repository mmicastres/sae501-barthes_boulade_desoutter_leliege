package com.example.hiker.ui.components

import HikersViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.AlertDialog
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hiker.R
import com.example.hiker.ui.theme.Beige
import com.example.hiker.ui.theme.Maron
import android.util.Log


@Composable
fun ConnectionPage(navController: NavController, viewModel: HikersViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var isLoggingIn by remember { mutableStateOf(false) }

    // Observer l'état de connexion
    val loginState = viewModel.loginState.collectAsState().value

    LaunchedEffect(loginState) {
        when (loginState) {
            HikersViewModel.LoginState.Success -> navController.navigate("profile")
            HikersViewModel.LoginState.Loading -> isLoggingIn = true
            HikersViewModel.LoginState.Failed -> {
                isLoggingIn = false
                showErrorDialog = true
            }
            else -> isLoggingIn = false
        }
    }

    if (showErrorDialog) {
        ErrorDialog(onDismiss = { showErrorDialog = false })
    }

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
                label = "email"
            )

            Spacer(modifier = Modifier.height(8.dp))

            StyledTextField(
                value = password,
                onValueChange = { password = it },
                label = "Mot de passe",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(navController, onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    viewModel.login(username, password)
                } else {
                    showErrorDialog = true
                }
            })
        }
    }
}

@Composable
fun StyledTextField(value: String, onValueChange: (String) -> Unit, label: String, isPassword: Boolean = false) {
    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        visualTransformation = visualTransformation,
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
fun CustomButton(navController: NavController, onClick: () -> Unit) {
    val buttonBackgroundColor = Maron
    val buttonContentColor = Color.White
    val outlinedButtonContentColor = Color.Black

    Button(
        onClick = onClick,
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

@Composable
fun ErrorDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Erreur") },
        text = { Text("Identifiant ou mot de passe incorrect") },
        confirmButton = {
            Button(onClick = onDismiss) { // Utilisez la lambda onDismiss pour fermer la popup
                Text("OK")
            }
        }
    )
}

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hiker.R
import com.example.hiker.ui.theme.Beige
import com.example.hiker.ui.theme.Maron

@Composable
fun InscriptionPage(navController: NavController, viewModel: HikersViewModel) {
    var pseudo by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val background = painterResource(id = R.drawable.background)
    var showInscriptionErrorDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var isLoggingIn by remember { mutableStateOf(false) }

    if (showInscriptionErrorDialog) {
        InscriptionErrorDialog(onDismiss = { showInscriptionErrorDialog = false })
    }

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

    Box(modifier = Modifier.fillMaxHeight()) {
        Image(
            painter = background,
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Inscription",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp,
                color = Color.Black
            ),
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
            InscriptionTextField(
                value = pseudo,
                onValueChange = { pseudo = it },
                label = "Pseudo"
            )

            Spacer(modifier = Modifier.height(8.dp))

            InscriptionTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email"
            )

            Spacer(modifier = Modifier.height(8.dp))

            InscriptionTextField(
                value = password,
                onValueChange = { password = it },
                label = "Mot de passe"
            )

            Spacer(modifier = Modifier.height(16.dp))

            InscriptionButton(navController, onClick = {
                if (pseudo.isNotBlank() && password.isNotBlank() && email.isNotBlank()) {
                    viewModel.inscription(pseudo, password, email)
                } else {
                    showInscriptionErrorDialog = true
                }
            })
        }
    }
}

@Composable
fun InscriptionTextField(value: String, onValueChange: (String) -> Unit, label: String) {
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
fun InscriptionButton(navController: NavController, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Maron, contentColor = Color.White),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(text = "S'inscrire", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White))
    }
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(
        onClick = { navController.popBackStack() },
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent, contentColor = Maron),
        border = BorderStroke(1.dp, Maron),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(text = "J'ai déjà un compte", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black))
    }
}

@Composable
fun InscriptionErrorDialog(onDismiss: () -> Unit) {
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

package com.example.hiker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hiker.R

@Composable
fun ConnectionPage() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val background = painterResource(id = R.drawable.backgroundconnection)

    Box(modifier = Modifier.fillMaxHeight()) {
        Image(
            painter = background,
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.9f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Connexion",
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            BasicTextField(
                value = username,
                onValueChange = { username = it },
                decorationBox = { innerTextField ->
                    Row(Modifier.background(Color.Yellow.copy(alpha = 0.5f), RoundedCornerShape(4.dp)).padding(16.dp)) {
                        if (username.isEmpty()) {
                            Text("Pseudo", color = Color.Gray)
                        }
                        innerTextField()
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                visualTransformation = if (password.isEmpty()) VisualTransformation.None else PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    Row(Modifier.background(Color.Yellow.copy(alpha = 0.5f), RoundedCornerShape(4.dp)).padding(16.dp)) {
                        if (password.isEmpty()) {
                            Text("Mot de passe", color = Color.Gray)
                        }
                        innerTextField()
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /* Handle login logic here */ }) {
                Text(text = "Se connecter")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = { /* Navigate to account creation screen */ }) {
                Text(text = "Je n'ai pas de compte")
            }
        }
    }
}

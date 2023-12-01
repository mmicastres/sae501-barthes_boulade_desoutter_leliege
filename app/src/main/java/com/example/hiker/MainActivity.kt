package com.example.hiker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Niveaux()
        }
    }
}

@Composable
fun Niveaux() {
    // Utilisez remember pour sauvegarder l'état même lorsque la composition est réexécutée
    val state = remember { mutableStateOf(ClassKil()) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(text = "Niveau actuel: ${state.value.niveau}")
        Text(text = "Grade: ${getGrade(state.value.niveau)}")
        Text(text = "Kilomètres parcourus: ${state.value.kilometres}")
        Button(
            onClick = {
                state.value = state.value.copy(kilometres = state.value.kilometres + 1)
                if (state.value.kilometres % 5 == 0) {
                    state.value = state.value.copy(niveau = state.value.niveau + 1)
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Parcourir 1 kilomètre")
        }
    }
}

data class ClassKil(
    val niveau: Int = 1,
    val kilometres: Int = 0
)

fun getGrade(niveau: Int): String {
    return when {
        niveau in 1..4 -> "Apprenti randonneur"
        niveau in 5..9 -> "Randonneur occasionnel"
        niveau in 10..14 -> "Randonneur"
        niveau in 15..19 -> "Randonneur expérimenté"
        niveau in 20..24 -> "Maître randonneur"
        niveau in 25..29 -> "Grand maître randonneur"
        niveau in 30..34 -> "Randonneur suprême"
        else -> "Éminent randonneur de l'ordre des Hikers"
    }
}

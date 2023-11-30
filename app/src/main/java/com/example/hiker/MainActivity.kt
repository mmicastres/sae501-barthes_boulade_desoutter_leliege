package com.example.hiker

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext



class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Niveaux()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Niveaux() {
    // Utilisez remember pour sauvegarder l'état même lorsque la composition est réexécutée
    val state = remember { mutableStateOf(ClassKil()) }

    // Mise à jour de l'état lorsque l'utilisateur parcourt 10 kilomètres
//    nbDeKilometreParcourus(state)


    Column(
        modifier = Modifier.fillMaxSize(),

        ) {
        Text(text = "Niveau actuel: ${state.value.niveau}")
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
/*
@RequiresApi(Build.VERSION_CODES.O)
private fun vibrer(context: Context) {
    val vibration = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (vibration.hasVibrator()) {
        val pattern = longArrayOf(0, 150, 100, 150)
        vibration.vibrate(pattern, -1)
    } else {
        Log.v("vibrer", "mon telephone ne peut pas vibrer");
    }

}

*/

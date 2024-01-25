package com.yourapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hiker.R
import kotlinx.coroutines.delay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.example.hiker.ui.theme.Beige

@Composable
fun DisplayCombatPage() {
    var timeLeft by remember { mutableStateOf(30) }
    var selectedElement by remember { mutableStateOf<Element?>(null) }

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Duel",
            fontSize = 24.sp
        )
        Text(
            text = timeLeft.toString().padStart(2, '0'),
            fontSize = 48.sp
        )
        Spacer(modifier = Modifier.weight(1f))

        // Afficher la carte sélectionnée en grand si un élément est sélectionné
        selectedElement?.let {
            LargeCardElement(element = it)
            Row {
                Button(
                    onClick = { /* Logique pour confirmer la sélection */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Beige)
                ) {
                    Text("Sélectionner")
                }
                Spacer(modifier = Modifier.width(8.dp)) // Espacement entre les boutons
                Button(
                    onClick = { selectedElement = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Beige)
                ) {
                    Text("Retour")
                }
            }
        } ?: run {
            // Sinon, afficher toutes les cartes pour la sélection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Element.values().forEach { element ->
                    Box(
                        modifier = Modifier
                            .weight(1f) // Chaque Box aura un poids égal dans la Row
                            .padding(4.dp), // Ajoutez du padding pour éviter que les images ne se touchent
                        contentAlignment = Alignment.Center
                    ) {
                        CardElement(element = element, isSelected = selectedElement == element) {
                            selectedElement = it
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LargeCardElement(element: Element) {
    // Cette Composable fonction va montrer l'élément sélectionné en grande taille
    val imageResource = when (element) {
        Element.Feu -> R.drawable.ic_fire
        Element.Terre -> R.drawable.ic_earth
        Element.Eau -> R.drawable.ic_water
    }

    Image(
        painter = painterResource(id = imageResource),
        contentDescription = element.name,
        modifier = Modifier
            .size(400.dp)
            .padding(16.dp)
    )
}

@Composable
fun CardElement(element: Element, isSelected: Boolean, onClick: (Element) -> Unit) {
    val imageResource = when (element) {
        Element.Feu -> R.drawable.ic_fire
        Element.Terre -> R.drawable.ic_earth
        Element.Eau -> R.drawable.ic_water
    }

    Box(
        modifier = Modifier
            .size(200.dp)
            .padding(8.dp)
            .clickable { onClick(element) },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = element.name,
            modifier = Modifier
                .fillMaxSize() // Remplir l'espace disponible dans la Box
                .padding(8.dp) // Conserver le padding pour l'espacement
        )
        if (isSelected) {
            // Ajouter un indicateur visuel si la carte est sélectionnée
        }
    }
}

enum class Element {
    Feu, Terre, Eau
}
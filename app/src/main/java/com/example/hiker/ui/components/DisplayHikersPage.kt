package com.example.hiker.ui.components

import HikersViewModel
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.example.hiker.ui.theme.Noir

data class Card(
    val id: Int,
    val miniaturaImageResource: Int,
    val detailedImageResource: Int,
    val isUnlocked: Boolean
)

@Composable
fun HikersPage(hikersViewModel: HikersViewModel) {
    var selectedImage by remember { mutableStateOf<Int?>(null) }

    // Observez les données des cartes comme un état
    val cards = hikersViewModel.cards.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Collection",
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )

        if (selectedImage != null) {
            ImageDisplay(image = selectedImage!!, onDismiss = { selectedImage = null })
        } else {
            // Passez les cartes en tant qu'état
            ImageGrid(cards = cards.value, onImageClick = { imageMinia ->
                val detailedImage = cards.value.firstOrNull { it.miniaturaImageResource == imageMinia }?.detailedImageResource
                detailedImage?.let { selectedImage = it }
            })
        }
    }
}

@Composable
fun ImageGrid(cards: List<Card>, onImageClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(cards) { card ->
            Image(
                painter = painterResource(id = card.miniaturaImageResource),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clickable { if (card.isUnlocked) onImageClick(card.miniaturaImageResource) },
                colorFilter = if (!card.isUnlocked) ColorFilter.tint(Noir) else null // Griser la carte si elle n'est pas débloquée
            )
        }
    }
}
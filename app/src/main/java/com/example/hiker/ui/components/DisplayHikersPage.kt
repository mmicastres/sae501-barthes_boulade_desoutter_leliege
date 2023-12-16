package com.example.hiker.ui.components

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
import com.example.hiker.R
import com.example.hiker.ui.theme.Noir

data class Card(val miniaturaImageResource: Int, val detailedImageResource: Int, val isUnlocked: Boolean)

@Composable
fun HikersPage() {
    var selectedImage by remember { mutableStateOf<Int?>(null) }

    // Liste initiale des cartes - À terme, cette liste viendra du backend
    val cards = listOf(
        Card(R.drawable.louis_minia, R.drawable.louis, true),
        Card(R.drawable.louna_minia, R.drawable.louna, false),
        Card(R.drawable.alexandre_minia, R.drawable.alexandre, false),
        Card(R.drawable.nino_minia, R.drawable.nino, false),
        Card(R.drawable.simpson_minia, R.drawable.simpson, false),
        Card(R.drawable.merlin_minia, R.drawable.merlin, false)
    )

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
            ImageGrid(cards, onImageClick = { imageMinia ->
                // Trouver l'image détaillée correspondant à l'image miniature cliquée
                val detailedImage = cards.firstOrNull { it.miniaturaImageResource == imageMinia }?.detailedImageResource
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
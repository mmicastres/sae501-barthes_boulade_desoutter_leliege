package com.example.hiker.ui.components

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

@Composable
fun HikersPage() {
    var selectedImage by remember { mutableStateOf<Int?>(null) }

    // Associer chaque image miniature à son image détaillée
    val imageMap = mapOf(
        R.drawable.louis_minia to R.drawable.louis,
        R.drawable.louna_minia to R.drawable.louna,
        R.drawable.alexandre_minia to R.drawable.alexandre,
        R.drawable.nino_minia to R.drawable.nino,
        R.drawable.simpson_minia to R.drawable.simpson,
        R.drawable.merlin_minia to R.drawable.merlin
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
            // Afficher l'image détaillée
            ImageDisplay(image = selectedImage!!, onDismiss = { selectedImage = null })
        } else {
            // Afficher la grille d'images miniatures
            ImageGrid(onImageClick = { imageMinia ->
                // Lorsqu'une image miniature est cliquée, définissez l'image détaillée à afficher
                selectedImage = imageMap[imageMinia]
            })
        }
    }
}



@Composable
fun ImageGrid(onImageClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Gardez 2 colonnes comme avant
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val images = listOf(
            R.drawable.louis_minia,
            R.drawable.louna_minia,
            R.drawable.alexandre_minia,
            R.drawable.nino_minia,
            R.drawable.simpson_minia,
            R.drawable.merlin_minia
        )
        items(images) { image ->
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                // Ajoutez le modificateur size pour ajuster la taille de l'image
                modifier = Modifier
                    .size(150.dp) // Supposons que la taille originale est 75.dp, la doubler revient à mettre 150.dp
                    .clickable { onImageClick(image) }
            )
        }
    }
}
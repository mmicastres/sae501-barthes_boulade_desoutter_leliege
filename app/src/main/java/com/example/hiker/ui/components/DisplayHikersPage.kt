package com.example.hiker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.hiker.R

@Composable
fun HikersPage() {
    var selectedImage by remember { mutableStateOf<Int?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Collection",
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )

        if (selectedImage == null) {
            ImageGrid(onImageClick = { image -> selectedImage = image })
        } else {
            ImageDisplay(image = selectedImage!!, onDismiss = { selectedImage = null })
        }
    }
}

@Composable
fun ImageGrid(onImageClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val images = listOf(
            R.drawable.louis,
            R.drawable.alexandre,
            R.drawable.louna,
            R.drawable.nino,
            R.drawable.simpson,
            R.drawable.merlin
        )
        items(images) { image ->
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.clickable { onImageClick(image) }
            )
        }
    }
}

@Composable
fun ImageDisplay(image: Int, onDismiss: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Selected image",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )

        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.Black
            )
        }
    }
}

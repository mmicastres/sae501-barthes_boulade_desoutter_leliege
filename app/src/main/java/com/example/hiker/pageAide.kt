package com.example.hiker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScreenPageAide() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Text(
                text = "Règle du jeu",
                fontFamily = FontFamily.Serif,
                fontSize = 25.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,

                )
        }
        Column() {
            Text(
                text = "Bonjour et bienvenu jeune randonneur dans Hiker.\n" +
                        "Pour jouer c'est très simple il vous suffit de marcher le plus possible pour augmenter de niveau.\n" +
                        "Attention, vous pouvez rencontrer d'autres joueurs au cours de votre périple. Vous pourrez les affronter en duel pour espérer collections une multitude de personnages.\n" +
                        "\n"
            )
            Text(
                text = "Rareté des Personnages :\n" +
                        " - Vert : Commun\n" +
                        " - Bleu : Rare\n" +
                        " - Violet : Epique\n" +
                        " - Or : Légendaire\n" +
                        "\n"
            )
            Text(
                text = "Comment marche un Duel : \n" +
                        " - Les deux joueurs sont assez proches\n" +
                        " - Les deux joueurs acceptent le duel\n" +
                        " - Les deux joueurs choisissent un élément (feu, eau ou terre)\n" +
                        " - Le combat commence :\n" +
                        "\t        - Le Feu bat l'Eau\n" +
                        "\t        - L'Eau bat la Terre\n" +
                        "        \t- La Terre bat le Feu\n" +
                        "        \t- En cas d'égalité le duel recommence\n" +
                        " - Nous avons un gagnant et il remporte une récompense (un personnage aléatoire)"
            )

        }

    }
}


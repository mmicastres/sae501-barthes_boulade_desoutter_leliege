import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.hiker.ui.theme.Maron
import kotlinx.coroutines.delay

@Composable
fun DuelRequestPopup(
    showDialog: Boolean,
    onChallengeAccepted: () -> Unit,
    onChallengeDeclined: () -> Unit,
    onDismissRequest: () -> Unit
) {
    if (!showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(text = "Joueur trouvé à proximité")
            },
            text = {
                Text(text = "Le défier ?")
            },
            confirmButton = {
                Button(
                    onClick = onChallengeAccepted,
                    colors = ButtonDefaults.buttonColors(containerColor = Maron)
                ) {
                    Text("Oui")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onChallengeDeclined,
                    colors = ButtonDefaults.textButtonColors(contentColor = Maron)
                ) {
                    Text("Non")
                }
            }
        )
    }
}

@Composable
fun YourMainScreen(viewModel: HikersViewModel, navController: NavController) {
    val wantDuel by viewModel.wantDuel.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var timerActive by remember { mutableStateOf(false) }
    val duelResult by viewModel.duelResult.collectAsState()

    LaunchedEffect(key1 = "anyKey") {
        showDialog = wantDuel ?: false
    }

    DuelRequestPopup(
        showDialog = showDialog,
        onChallengeAccepted = {
            viewModel.modifierStatutDuel(true)
            showDialog = true
            timerActive = true
        },
        onChallengeDeclined = {
            showDialog = true
        },
        onDismissRequest = {
            showDialog = true
        }
    )

    // Gérer le timer
    LaunchedEffect(key1 = timerActive) {
        if (timerActive) {
            delay(10000)
            viewModel.verifierDuel()
            timerActive = false
        }
    }

    // Réagir aux changements de duelResult
    LaunchedEffect(duelResult) {
        duelResult?.let {
            if (it.duel) {
                navController.navigate("combat") // Naviguer vers la page de combat
            }
        }
    }
}
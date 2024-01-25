import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.hiker.ui.theme.Jaune
import com.example.hiker.ui.theme.Maron

@Composable
fun DuelRequestPopup(
    showDialog: Boolean,
    onChallengeAccepted: () -> Unit,
    onChallengeDeclined: () -> Unit,
    onDismissRequest: () -> Unit
) {
    if (showDialog) {
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
fun YourMainScreen() {
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = "anyKey") {
        showDialog = true
    }

    DuelRequestPopup(
        showDialog = showDialog,
        onChallengeAccepted = {
            showDialog = false
        },
        onChallengeDeclined = {
            showDialog = false
        },
        onDismissRequest = {
            showDialog = false
        }
    )

    // ... Rest of your main screen UI
}

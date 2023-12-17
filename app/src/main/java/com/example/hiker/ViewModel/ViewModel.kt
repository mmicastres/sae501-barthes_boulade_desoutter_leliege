import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiker.R
import com.example.hiker.ui.components.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HikersViewModel : ViewModel() {
    // État pour gérer les cartes
    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards

    init {
        loadCards()
    }

    // Fonction pour charger les cartes (simulée ici avec des données statiques)
    private fun loadCards() {
        viewModelScope.launch {
            // Simuler un chargement de données (ici, données statiques)
            val loadedCards = listOf(
                Card(R.drawable.louis_minia, R.drawable.louis, true),
                Card(R.drawable.louna_minia, R.drawable.louna, false),
                Card(R.drawable.alexandre_minia, R.drawable.alexandre, false),
                Card(R.drawable.nino_minia, R.drawable.nino, false),
                Card(R.drawable.simpson_minia, R.drawable.simpson, false),
                Card(R.drawable.merlin_minia, R.drawable.merlin, false)
            )
            _cards.value = loadedCards
        }
    }

    // Fonction pour gérer le déverrouillage des cartes (à implémenter)
    fun unlockCard(cardId: Int) {
        // Logique pour déverrouiller une carte et mettre à jour l'état
    }
}

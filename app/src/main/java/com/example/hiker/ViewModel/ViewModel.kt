import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiker.R
import com.example.hiker.ui.components.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// Modèle de données pour l'utilisateur
data class User(val username: String, val password: String)

// Interface Retrofit pour les appels API
interface UserApiService {
    @POST("utilisateurs")
    suspend fun getUser(@Query("username") username: String): User
}

class HikersViewModel : ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://desouttter.alwaysdata.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(UserApiService::class.java)

    // États de connexion
    private val _loginState = MutableStateFlow(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    enum class LoginState {
        Idle, Loading, Success, Failed
    }

    // État pour gérer les cartes
    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards


    //partie récupération des cartes
    init {
        loadCards()
    }
//    fun unlockCard(cardId: Int) {
//        // Logique pour déverrouiller une carte et mettre à jour l'état
//    }

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
    // partie Connetion
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val user = service.getUser(username)
                if (user.password == password) {
                    _loginState.value = LoginState.Success
                } else {
                    _loginState.value = LoginState.Failed
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Failed
            }
        }
    }

}
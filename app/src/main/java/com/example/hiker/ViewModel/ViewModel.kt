import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiker.R
import com.example.hiker.ui.components.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// Interface Retrofit pour les appels API
interface UserApiService {
    @POST("connexion")
    suspend fun login(@Body credentials: LoginCredentials): LoginResponse

    @POST("inscription")
    suspend fun inscription(@Body credentials: InscriptionCredentials): InscriptionResponse

    @GET("profil")
    suspend fun getProfil(@Query("userId") userId: String): Profil

    @GET("collection")
    suspend fun getCollection(@Query("userId") userId: String): Collection
}

data class LoginCredentials(val email: String, val mdp: String)
data class LoginResponse(val success: Boolean, val token: String?)
data class InscriptionCredentials(val email: String, val password: String, val identifiant: String)
data class InscriptionResponse(val success: Boolean)
data class Profil(
    val distanceTotaleParcourue: Double,
    val distanceJournaliereParcourue: Double,
    val niveau: Int,
    val rank: Int,
    val pseudo: String,
    val duelsGagnes: Int
)

data class Collection(val cartesPossedees: List<Card>)

class HikersViewModel : ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://desouttter.alwaysdata.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(UserApiService::class.java)

    // États de connexion
    private val _loginState = MutableStateFlow(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState
    private val _loginToken = MutableStateFlow<String?>(null)
    val loginToken: StateFlow<String?> = _loginToken

    fun logout() {
        _loginState.value = LoginState.Idle
        _loginToken.value = null
    }

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
    fun login(email: String, mdp: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = service.login(LoginCredentials(email, mdp))
                if (!response.token.isNullOrEmpty()) {
                    _loginToken.value = response.token
                    _loginState.value = LoginState.Success
                } else {
                    _loginState.value = LoginState.Failed
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Failed
            }
        }
    }

    fun inscription(email: String, mdp: String, identifiant: String) {
        viewModelScope.launch {
            try {
                val response = service.inscription(InscriptionCredentials(email, mdp, identifiant))
                // Gérer la réponse de l'inscription
            } catch (e: Exception) {
                // Gérer les exceptions
            }
        }
    }

    // Fonction pour obtenir le profil
    fun getProfil(userId: String) {
        viewModelScope.launch {
            try {
                val profil = service.getProfil(userId)
                // Gérer les données du profil
            } catch (e: Exception) {
                // Gérer les exceptions
            }
        }
    }

    // Fonction pour obtenir la collection
    fun getCollection(userId: String) {
        viewModelScope.launch {
            try {
                val collection = service.getCollection(userId)
                // Gérer les données de la collection
            } catch (e: Exception) {
                // Gérer les exceptions
            }
        }
    }
}
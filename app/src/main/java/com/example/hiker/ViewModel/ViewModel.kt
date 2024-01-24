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
import retrofit2.http.Path
import retrofit2.http.Query

// Interface Retrofit pour les appels API
interface UserApiService {

    @POST("connexion")
    suspend fun login(@Body credentials: LoginCredentials): LoginResponse

    @POST("inscription")
    suspend fun inscription(@Body credentials: InscriptionCredentials): InscriptionResponse

    @GET("utilisateurs/{id_utilisateur}")
    suspend fun getUserInfo(
        @Path("id_utilisateur") userId: String,
        @Query("token") token: String
    ): UserInfoResponse
}

data class UserInfoResponse(
    val duelsGagnes: Int,
    val experience: Int,
    val totalKmParcourus: Double,
    val kmParcourusJour: Double,
    val personnagesObtenus: List<Character>
)
data class LoginCredentials(
    val email: String,
    val mdp: String)
data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val id_util: Int?)
data class InscriptionCredentials(
    val email: String,
    val mdp: String,
    val pseudo: String)
data class InscriptionResponse(
    val success: Boolean)
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

    private val _loginState = MutableStateFlow(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState
    private val _loginToken = MutableStateFlow<String?>(null)
    val loginToken: StateFlow<String?> = _loginToken
    private val _inscriptionState = MutableStateFlow(InscriptionState.Idle)
    val inscriptionState: StateFlow<InscriptionState> = _inscriptionState


    fun logout() {
        _loginState.value = LoginState.Idle
        _loginToken.value = null
    }

    enum class LoginState {
        Idle, Loading, Success, Failed
    }

    enum class InscriptionState {
        Idle, Loading, Success, Failed
    }

    // État pour gérer les cartes
    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards

    // gestion des details utilisateurs
    private val _userInfo = MutableStateFlow<UserInfoResponse?>(null)
    val userInfo: StateFlow<UserInfoResponse?> = _userInfo


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
                    Log.i("test", "token : ${response.token}")
                    Log.i("test", "id_util : ${response.id_util}")
                    _loginToken.value = response.token
                    _loginState.value = LoginState.Success
                    response.id_util?.let { getUserInfo(it, response.token) }
                } else {
                    _loginState.value = LoginState.Failed
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Failed
            }
        }
    }

    fun inscription(pseudo: String, mdp: String, email: String ) {
        viewModelScope.launch {
            _inscriptionState.value = InscriptionState.Loading
            try {
                Log.i("Inscription", "email: $email, mdp : $mdp, pseudo : $pseudo")
                val response = service.inscription(InscriptionCredentials(email, mdp, pseudo))
                if (response.success) {
                    login(email, mdp)
                }
            } catch (e: Exception) { 
                _inscriptionState.value = InscriptionState.Failed
                Log.e("Inscription", "Erreur d'inscription: ${e.message}")
            }
        }
    }

    fun getUserInfo(userId: Int, token: String) {
        viewModelScope.launch {
            try {
                val userInfoResponse = service.getUserInfo(userId.toString(), token)
                _userInfo.value = userInfoResponse
                val totalDistanceFromServer = userInfoResponse.totalKmParcourus.toFloat()
                Log.i("test", "infos : ${_userInfo.value}")
                Log.i("test", "infos : ${_userInfo.value!!.totalKmParcourus}")
                // Vous pouvez maintenant utiliser _userInfo.value pour accéder aux informations de l'utilisateur
            } catch (e: Exception) {
                Log.e("getUserInfo", "Erreur lors de la récupération des informations de l'utilisateur: ${e.message}")
                // Gérer l'exception si nécessaire
            }
        }
    }

    // Fonction pour obtenir la collection
    fun getCollection(userId: Int, token: String) {
        viewModelScope.launch {
            try {
                val collection = service.getUserInfo(userId.toString(), token)
                // Gérer les données de la collection
            } catch (e: Exception) {
                // Gérer les exceptions
            }
        }
    }
}
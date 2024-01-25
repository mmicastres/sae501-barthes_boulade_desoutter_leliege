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
import retrofit2.http.PUT
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

    @GET("utilisateurs/{id_utilisateur}/myperso")
    suspend fun getCollection(
        @Path("id_utilisateur") userId: String,
        @Query("token") token: String
    ): CollectionResponse

    @PUT("utilisateurs/{id_utilisateur}/kilometre")
    suspend fun postLocation(
        @Path("id_utilisateur") userId: String,
        @Query("token") token: String,
        @Body credentials: LocationCredentials
    ): LocationResponse
}

data class UserInfoResponse(
    val pseudo: String,
    val duel_gagne: Int,
    val exp: Int,
    val nbr_km_total: Double,
    val nbr_km_today: Double,
    val liste_perso: List<Int>
)
data class CollectionResponse(
    val liste_perso: List<Int>
)
data class LocationResponse(
    val success: Boolean
)
data class LoginCredentials(
    val email: String,
    val mdp: String
)
data class LocationCredentials(
    val nbr_km_total: Int,
    val nbr_km_today: Int,
    val localisation: Localisation
)
data class Localisation(
    val x: Float,
    val y: Float
)
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
    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId
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
        Idle, Loading, Failed
    }

    // État pour gérer les cartes
    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards

    // gestion des details utilisateurs
    private val _userInfo = MutableStateFlow<UserInfoResponse?>(null)
    val userInfo: StateFlow<UserInfoResponse?> = _userInfo


    private val allCards = listOf(
        Card(1, R.drawable.louis_minia, R.drawable.louis, false),
        Card(1, R.drawable.louna_minia, R.drawable.louna, false),
        Card(1, R.drawable.alexandre_minia, R.drawable.alexandre, false),
        Card(1, R.drawable.nino_minia, R.drawable.nino, false),
        Card(1, R.drawable.simpson_minia, R.drawable.simpson, false),
        Card(1, R.drawable.merlin_minia, R.drawable.merlin, false),
    )

    // Fonction pour charger les cartes (simulée ici avec des données statiques)
    private fun loadUserCards(userId: Int, token: String) {
        viewModelScope.launch {
            try {
                val userCollection = service.getCollection(userId.toString(), token).liste_perso
                val updatedCards = allCards.map { card ->
                    if (card.id in userCollection) card.copy(isUnlocked = true) else card.copy(isUnlocked = false)
                }
                _cards.value = updatedCards
            } catch (e: Exception) {
                // Gérer les exceptions
            }
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
                    _loginToken.value = response.token
                    _userId.value = response.id_util
                    response.id_util?.let {
                        getUserInfo(it, response.token)
                        loadUserCards(it, response.token)
                    }
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

    private fun getUserInfo(userId: Int, token: String) {
        viewModelScope.launch {
            try {
                val userInfoResponse = service.getUserInfo(userId.toString(), token)
                _userInfo.value = userInfoResponse
                Log.i("test", "infos : ${_userInfo.value}")
                Log.i("test", "infos : ${_userInfo.value!!.nbr_km_total}")
            } catch (e: Exception) {
                Log.e("getUserInfo", "Erreur lors de la récupération des informations de l'utilisateur: ${e.message}")
            }
        }
    }

    fun postLocation(latitude: Float, longitude: Float, nbrKmTotal: Float, nbrKmToday: Float) {
        viewModelScope.launch {
            _userId.value?.let { userId ->
                _loginToken.value?.let { token ->
                    Log.i("postlocation", "id : ${userId} token : ${token}")
                    Log.i("postlocation", "total km : ${nbrKmTotal} today km : ${nbrKmToday}")
                    Log.i("postlocation", "x et y : ${Localisation(x = latitude, y = longitude)}")
                    try {
                        val locationCredentials = LocationCredentials(
                            nbr_km_total = nbrKmTotal.toInt(),
                            nbr_km_today = nbrKmToday.toInt(),
                            localisation = Localisation(x = latitude, y = longitude)
                        )
                        val response = service.postLocation(userId.toString(), token, locationCredentials)
                        if (response.success) {
                            Log.i("postLocation", "Mise à jour de la localisation réussie")
                        } else {
                            Log.e("postLocation", "Échec de la mise à jour de la localisation")
                        }
                    } catch (e: Exception) {
                        Log.e("postLocation", "Erreur lors de la mise à jour de la localisation: ${e.message}")
                    }
                }
            }
        }
    }
}
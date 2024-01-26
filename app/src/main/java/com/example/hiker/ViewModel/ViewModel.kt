import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiker.R
import com.example.hiker.ui.components.Card
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    private val _totalDistance = MutableStateFlow<Float?>(null)
    val totalDistance: StateFlow<Float?> = _totalDistance

    private val _utilisateursProximite = MutableStateFlow<List<UtilisateurProximite>>(emptyList())
    val utilisateursProximite: StateFlow<List<UtilisateurProximite>> = _utilisateursProximite

    private val _wantDuel = MutableStateFlow<Boolean?>(null)
    val wantDuel: StateFlow<Boolean?> = _wantDuel

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards

    private val _userInfo = MutableStateFlow<UserInfoResponse?>(null)
    val userInfo: StateFlow<UserInfoResponse?> = _userInfo

    private val _duelResult = MutableStateFlow<DuelResponse?>(null)
    val duelResult: StateFlow<DuelResponse?> = _duelResult


    fun setTotalDistance(distance: Float) {
        _totalDistance.value = distance
    }

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


    private val allCards = listOf(
        Card(1, R.drawable.louis_minia, R.drawable.louis, false),
        Card(2, R.drawable.louna_minia, R.drawable.louna, false),
        Card(3, R.drawable.alexandre_minia, R.drawable.alexandre, false),
        Card(4, R.drawable.nino_minia, R.drawable.nino, false),
        Card(5, R.drawable.simpson_minia, R.drawable.simpson, false),
        Card(6, R.drawable.merlin_minia, R.drawable.merlin, false),
    )

    // Fonction pour charger les cartes (simulée ici avec des données statiques)
    private fun loadUserCards(userId: Int, token: String) {
        viewModelScope.launch {
            try {
                val userCollection = service.getCollection(userId.toString(), token).liste_perso
                val updatedCards = allCards.map { card ->
                    if (card.id in userCollection) card.copy(isUnlocked = true) else card.copy(
                        isUnlocked = false
                    )
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
                        detecterUtilisateursProximitePeriodiquement()
                    }
                } else {
                    _loginState.value = LoginState.Failed
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Failed
            }
        }
    }

    fun inscription(pseudo: String, mdp: String, email: String) {
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
                Log.e(
                    "getUserInfo",
                    "Erreur lors de la récupération des informations de l'utilisateur: ${e.message}"
                )
            }
        }
    }

    fun postLocation(latitude: Float, longitude: Float, nbrKmToday: Float) {
        viewModelScope.launch {
            _userId.value?.let { userId ->
                _loginToken.value?.let { token ->
                    _totalDistance.value?.let { nbrKmTotal ->
                        Log.i("postlocation", "id : ${userId} token : ${token}")
                        Log.i("postlocation", "total km : ${nbrKmTotal} today km : ${nbrKmToday}")
                        Log.i(
                            "postlocation",
                            "x et y : ${Localisation(x = latitude, y = longitude)}"
                        )
                        try {
                            val locationCredentials = LocationCredentials(
                                nbr_km_total = nbrKmTotal.toInt(),
                                nbr_km_today = nbrKmToday.toInt(),
                                localisation = Localisation(x = latitude, y = longitude)
                            )
                            val response =
                                service.postLocation(userId.toString(), token, locationCredentials)
                            if (response.success) {
                                Log.i("postLocation", "Mise à jour de la localisation réussie")
                            } else {
                                Log.e("postLocation", "Échec de la mise à jour de la localisation")
                            }
                        } catch (e: Exception) {
                            Log.e(
                                "postLocation",
                                "Erreur lors de la mise à jour de la localisation: ${e.message}"
                            )
                        }
                    }
                }
            }
        }
    }

    fun detecterUtilisateursProximitePeriodiquement() {
        viewModelScope.launch {
            while (_loginState.value != LoginState.Idle) {
                delay(5000)

                _userId.value?.let { userId ->
                    detecterUtilisateursProximite(userId)
                }
            }
        }
    }

    fun detecterUtilisateursProximite(userId: Int) {
        viewModelScope.launch {
            try {
                _userId.value?.let { userId ->
                    val response = service.detecterUtilisateursProximite(userId)
                    if (response.isSuccessful && response.body() != null) {
                        _utilisateursProximite.value = response.body()!!.utilisateursProximite
                        Log.i("detetion", "Succes réponse : ${_utilisateursProximite.value}")
                        Log.i("detetion", "Wantduel : ${response.body()!!.utilisateursProximite.first().want_duel}")
                        if (!response.body()!!.utilisateursProximite.first().want_duel){
                            Log.i("True", "want duel = ${response.body()!!.utilisateursProximite.first().want_duel}")
                        } else {
                            _wantDuel.value = response.body()!!.utilisateursProximite.first().want_duel
                        }
                    } else {
                        Log.e("API Error", "Erreur: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("API Error", "Erreur lors de la détection des utilisateurs à proximité: ${e.message}")
            }
        }
    }

    fun modifierStatutDuel(wantDuel: Boolean) {
        viewModelScope.launch {
            _userId.value?.let { userId ->
                _loginToken.value?.let { token ->
                    try {
                        val response = service.modifierStatutDuel(
                            userId,
                            token,
                            WantDuelRequest(wantDuel)
                        )
                        if (response.isSuccessful) {
                            Log.i("ModifierStatutDuel", "Succès : ${response.body()?.message}")
                            _wantDuel.value = wantDuel
                        } else {
                            Log.e("ModifierStatutDuel", "Erreur : ${response.errorBody()?.string()}")
                        }
                    } catch (e: Exception) {
                        Log.e("ModifierStatutDuel", "Exception : ${e.message}")
                    }
                }
            }
        }
    }

    fun verifierDuel() {
        viewModelScope.launch {
            _userId.value?.let { userId ->
                _loginToken.value?.let { token ->
                    try {
                        val response = service.verifierDuel(userId, token)
                        if (response.isSuccessful && response.body() != null) {
                            val duelResponse = response.body()!!
                            // Mettre à jour un état ici pour que l'interface utilisateur puisse observer
                            _duelResult.value = DuelResponse(duelResponse.duel, duelResponse.message)
                            Log.i("VerifierDuel", "Réponse : ${duelResponse.message}")
                        } else {
                            Log.e("VerifierDuel", "Erreur : ${response.errorBody()?.string()}")
                        }
                    } catch (e: Exception) {
                        Log.e("VerifierDuel", "Exception : ${e.message}")
                    }
                }
            }
        }
    }
}
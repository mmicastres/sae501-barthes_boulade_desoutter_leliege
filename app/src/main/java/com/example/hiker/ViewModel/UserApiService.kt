import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @PUT("utilisateurs/{id_utilisateur}/kilometres")
    suspend fun postLocation(
        @Path("id_utilisateur") userId: String,
        @Query("token") token: String,
        @Body credentials: LocationCredentials
    ): LocationResponse

    @GET("utilisateurs/{idutil}/proximite")
    suspend fun detecterUtilisateursProximite(
        @Path("idutil") idUtilisateur: Int
    ): Response<UtilisateursProximiteResponse>

    @PUT("utilisateurs/{idutil}/duel")
    suspend fun modifierStatutDuel(
        @Path("idutil") idUtilisateur: Int,
        @Query("token") token: String,
        @Body wantDuelRequest: WantDuelRequest
    ): Response<GenericResponse>

    @GET("duels/{idDuel}/reponse")
    suspend fun verifierDuel(
        @Path("idDuel") idDuel: Int,
        @Query("token") token: String
    ): Response<DuelResponse>
}


data class DuelResponse(
    val duel: Boolean,
    val message: String
)

data class GenericResponse(
    val success: Boolean,
    val message: String
)

data class WantDuelRequest(
    val want_duel: Boolean
)

data class UtilisateursProximiteResponse(
    val utilisateursProximite: List<UtilisateurProximite>
)
data class UtilisateurProximite(
    val id_util: Int,
    val pseudo: String,
    val distance: Double,
    val want_duel: Boolean
)

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
    val id_util: Int?
)

data class InscriptionCredentials(
    val email: String,
    val mdp: String,
    val pseudo: String
)

data class InscriptionResponse(
    val success: Boolean
)


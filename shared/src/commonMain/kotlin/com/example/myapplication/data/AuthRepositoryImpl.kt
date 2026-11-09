package com.example.myapplication.data

import co.touchlab.kermit.Logger
import com.example.myapplication.data.remote.AuthApi
import com.example.myapplication.domain.AuthRepository
import com.example.myapplication.domain.TokenStorage
import com.example.myapplication.domain.model.AuthError
import com.example.myapplication.domain.model.AuthException
import com.example.myapplication.domain.model.AuthTokens
import com.example.myapplication.domain.model.UserProfile
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    private val log = Logger.withTag("AuthRepository")

    override suspend fun login(username: String, password: String): Result<Unit> =
        safeCall(clientError = AuthError.INVALID_CREDENTIALS) {
            val response = api.login(username, password)
            tokenStorage.saveTokens(AuthTokens(response.accessToken, response.refreshToken))
            api.clearCachedTokens()
            log.i { "Login ok for ${response.username}" }
        }

    override suspend fun getProfile(): Result<UserProfile> =
        safeCall(clientError = AuthError.UNAUTHORIZED) {
            val profile = api.getProfile()
            UserProfile(profile.username, profile.firstName)
        }

    override suspend fun logout() {
        tokenStorage.clear()
        api.clearCachedTokens()
        log.i { "Logged out" }
    }

    override suspend fun isAuthorized(): Boolean = tokenStorage.hasTokens()

    private suspend fun <T> safeCall(clientError: AuthError, block: suspend () -> T): Result<T> =
        try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: ClientRequestException) {
            Result.failure(AuthException(clientError, e))        // 4xx
        } catch (e: ServerResponseException) {
            Result.failure(AuthException(AuthError.SERVER, e))   // 5xx
        } catch (e: SerializationException) {
            Result.failure(AuthException(AuthError.UNKNOWN, e))
        } catch (e: Exception) {
            Result.failure(AuthException(AuthError.NETWORK, e))  // немає мережі, таймаут
        }
}
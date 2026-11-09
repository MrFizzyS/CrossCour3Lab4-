package com.example.myapplication.data.remote

import co.touchlab.kermit.Logger
import com.example.myapplication.domain.TokenStorage
import com.example.myapplication.domain.model.AuthTokens
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val BASE_URL = "https://dummyjson.com"
private val log = Logger.withTag("Auth")

private val appJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

fun createPlainClient(): HttpClient = HttpClient {
    expectSuccess = true
    install(ContentNegotiation) { json(appJson) }
    install(HttpTimeout) { requestTimeoutMillis = 15_000 }
}


fun createAuthClient(tokenStorage: TokenStorage): HttpClient = HttpClient {
    expectSuccess = true
    install(ContentNegotiation) { json(appJson) }
    install(HttpTimeout) { requestTimeoutMillis = 15_000 }
    install(Auth) {
        bearer {
            loadTokens {
                tokenStorage.getTokens()?.let { BearerTokens(it.accessToken, it.refreshToken) }
            }
            refreshTokens {
                val refresh = tokenStorage.getTokens()?.refreshToken
                    ?: return@refreshTokens null
                try {
                    log.i { "Access token expired, refreshing..." }
                    val response: RefreshResponse = client.post("$BASE_URL/auth/refresh") {
                        markAsRefreshTokenRequest()
                        contentType(ContentType.Application.Json)
                        setBody(RefreshRequest(refresh))
                    }.body()
                    val fresh = AuthTokens(response.accessToken, response.refreshToken)
                    tokenStorage.saveTokens(fresh)
                    log.i { "Token refreshed" }
                    BearerTokens(fresh.accessToken, fresh.refreshToken)
                } catch (e: Exception) {
                    log.e(e) { "Refresh failed, session is over" }
                    tokenStorage.clear()
                    null
                }
            }
            sendWithoutRequest { request -> request.url.host == "dummyjson.com" }
        }
    }
}
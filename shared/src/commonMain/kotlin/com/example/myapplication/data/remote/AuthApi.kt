package com.example.myapplication.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthApi(
    private val plainClient: HttpClient,
    private val authClient: HttpClient
) {
    suspend fun login(username: String, password: String): LoginResponse =
        plainClient.post("$BASE_URL/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username, password))
        }.body()

    suspend fun getProfile(): ProfileResponse =
        authClient.get("$BASE_URL/auth/me").body()

    fun clearCachedTokens() {
        authClient.authProvider<BearerAuthProvider>()?.clearToken()
    }
}
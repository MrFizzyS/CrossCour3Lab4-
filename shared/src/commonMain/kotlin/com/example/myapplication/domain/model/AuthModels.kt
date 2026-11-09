package com.example.myapplication.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokens(
    val accessToken: String = "",
    val refreshToken: String = ""
)

data class UserProfile(val username: String, val firstName: String)

enum class AuthError { INVALID_CREDENTIALS, NETWORK, SERVER, UNAUTHORIZED, UNKNOWN }

class AuthException(val error: AuthError, cause: Throwable? = null) : Exception(error.name, cause)
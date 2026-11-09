package com.example.myapplication.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int = 30
)

@Serializable
data class LoginResponse(
    val id: Int = 0,
    val username: String = "",
    val firstName: String = "",
    val accessToken: String,
    val refreshToken: String
)

@Serializable
data class RefreshRequest(
    val refreshToken: String,
    val expiresInMins: Int = 30
)

@Serializable
data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String
)

@Serializable
data class ProfileResponse(
    val id: Int = 0,
    val username: String = "",
    val firstName: String = "",
    val lastName: String = ""
)
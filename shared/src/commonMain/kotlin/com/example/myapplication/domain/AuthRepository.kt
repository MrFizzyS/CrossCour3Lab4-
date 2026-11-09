package com.example.myapplication.domain

import com.example.myapplication.domain.model.UserProfile

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun getProfile(): Result<UserProfile>
    suspend fun logout()
    suspend fun isAuthorized(): Boolean
}
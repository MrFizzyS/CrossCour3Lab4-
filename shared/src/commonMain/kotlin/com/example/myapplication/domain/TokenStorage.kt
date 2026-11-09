package com.example.myapplication.domain

import com.example.myapplication.domain.model.AuthTokens

interface TokenStorage {
    suspend fun getTokens(): AuthTokens?
    suspend fun saveTokens(tokens: AuthTokens)
    suspend fun clear()
    suspend fun hasTokens(): Boolean
}
package com.example.myapplication.data

import com.example.myapplication.domain.TokenStorage
import com.example.myapplication.domain.model.AuthTokens
import eu.anifantakis.lib.ksafe.KSafe

class KSafeTokenStorage(private val ksafe: KSafe) : TokenStorage {

    override suspend fun getTokens(): AuthTokens? =
        ksafe.get(KEY, AuthTokens()).takeIf { it.accessToken.isNotEmpty() }

    override suspend fun saveTokens(tokens: AuthTokens) = ksafe.put(KEY, tokens)

    override suspend fun clear() = ksafe.delete(KEY)

    override suspend fun hasTokens(): Boolean = getTokens() != null

    private companion object { const val KEY = "auth_tokens" }
}
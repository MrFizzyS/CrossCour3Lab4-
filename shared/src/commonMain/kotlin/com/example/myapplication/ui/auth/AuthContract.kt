package com.example.myapplication.ui.auth

import com.example.myapplication.domain.usecase.FieldError

data class AuthState(
    val username: String = "",
    val password: String = "",
    val usernameError: FieldError? = null,
    val passwordError: FieldError? = null,
    val isPasswordVisible: Boolean = false
)

sealed interface AuthIntent {
    data class UsernameChanged(val value: String) : AuthIntent
    data class PasswordChanged(val value: String) : AuthIntent
    data object TogglePasswordVisibility : AuthIntent
    data object SignIn : AuthIntent
}

sealed interface AuthEffect {
    data object NavigateToTodos : AuthEffect
}
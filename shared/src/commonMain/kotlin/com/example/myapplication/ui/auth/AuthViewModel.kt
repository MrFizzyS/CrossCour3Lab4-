package com.example.myapplication.ui.auth

import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.usecase.ValidatePasswordUseCase
import com.example.myapplication.domain.usecase.ValidateUsernameUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class AuthViewModel(
    private val validateUsername: ValidateUsernameUseCase,
    private val validatePassword: ValidatePasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _effects = Channel<AuthEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.UsernameChanged ->
                _state.update { it.copy(username = intent.value, usernameError = null) }
            is AuthIntent.PasswordChanged ->
                _state.update { it.copy(password = intent.value, passwordError = null) }
            AuthIntent.TogglePasswordVisibility ->
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            AuthIntent.SignIn -> signIn()
        }
    }

    private fun signIn() {
        val current = _state.value
        val usernameError = validateUsername(current.username)
        val passwordError = validatePassword(current.password)
        _state.update { it.copy(usernameError = usernameError, passwordError = passwordError) }
        if (usernameError == null && passwordError == null) {
            _effects.trySend(AuthEffect.NavigateToTodos)
        }
    }
}
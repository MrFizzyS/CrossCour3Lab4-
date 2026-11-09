package com.example.myapplication.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.AuthError
import com.example.myapplication.domain.model.AuthException
import com.example.myapplication.domain.usecase.LoginUseCase
import com.example.myapplication.domain.usecase.ValidatePasswordUseCase
import com.example.myapplication.domain.usecase.ValidateUsernameUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(
    private val validateUsername: ValidateUsernameUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val login: LoginUseCase
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
        if (current.isLoading) return   // захист від подвійного натискання

        val usernameError = validateUsername(current.username)
        val passwordError = validatePassword(current.password)
        _state.update { it.copy(usernameError = usernameError, passwordError = passwordError) }
        if (usernameError != null || passwordError != null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            login(current.username.trim(), current.password).fold(
                onSuccess = { _effects.send(AuthEffect.NavigateToHome) },
                onFailure = { e ->
                    val error = (e as? AuthException)?.error ?: AuthError.UNKNOWN
                    _effects.send(AuthEffect.ShowError(error))
                }
            )
            _state.update { it.copy(isLoading = false) }
        }
    }
}
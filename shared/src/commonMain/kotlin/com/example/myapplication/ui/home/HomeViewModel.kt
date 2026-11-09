package com.example.myapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.AuthError
import com.example.myapplication.domain.model.AuthException
import com.example.myapplication.domain.usecase.GetProfileUseCase
import com.example.myapplication.domain.usecase.LogoutUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeState(val displayName: String? = null, val isLoading: Boolean = true)

sealed interface HomeEffect {
    data object NavigateToAuth : HomeEffect
}

class HomeViewModel(
    private val getProfile: GetProfileUseCase,
    private val logout: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effects = Channel<HomeEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init { loadProfile() }

    private fun loadProfile() {
        viewModelScope.launch {
            getProfile().fold(
                onSuccess = { profile ->
                    _state.update {
                        it.copy(
                            displayName = profile.firstName.ifBlank { profile.username },
                            isLoading = false
                        )
                    }
                },
                onFailure = { e ->
                    if ((e as? AuthException)?.error == AuthError.UNAUTHORIZED) {
                        // Сесія завершилась і оновлення токена не вдалось
                        logout()
                        _effects.send(HomeEffect.NavigateToAuth)
                    } else {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            )
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            logout()
            _effects.send(HomeEffect.NavigateToAuth)
        }
    }
}
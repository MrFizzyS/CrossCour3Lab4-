package com.example.myapplication.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecase.IsOnboardingCompletedUseCase
import com.example.myapplication.domain.usecase.IsUserAuthorizedUseCase
import com.example.myapplication.domain.usecase.ResetOnboardingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainState(
    val onboardingCompleted: Boolean,
    val isAuthorized: Boolean? = null
)

class MainViewModel(
    isOnboardingCompleted: IsOnboardingCompletedUseCase,
    private val isUserAuthorized: IsUserAuthorizedUseCase,
    private val resetOnboarding: ResetOnboardingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState(onboardingCompleted = isOnboardingCompleted()))
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val authorized = isUserAuthorized()
            _state.update { it.copy(isAuthorized = authorized) }
        }
    }

    fun onTestOnboarding() = resetOnboarding()
}
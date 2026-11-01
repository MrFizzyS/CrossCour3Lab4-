package com.example.myapplication.ui.main

import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.usecase.IsOnboardingCompletedUseCase
import com.example.myapplication.domain.usecase.ResetOnboardingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MainState(val onboardingCompleted: Boolean)

class MainViewModel(
    isOnboardingCompleted: IsOnboardingCompletedUseCase,
    private val resetOnboarding: ResetOnboardingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState(isOnboardingCompleted()))
    val state: StateFlow<MainState> = _state.asStateFlow()

    fun onTestOnboarding() = resetOnboarding()
}
package com.example.myapplication.ui.onboarding

import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.usecase.SaveOnboardingCompletedUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class OnboardingViewModel(
    private val saveOnboardingCompleted: SaveOnboardingCompletedUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _effects = Channel<OnboardingEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.PageChanged -> _state.update { it.copy(page = intent.page) }
            OnboardingIntent.Next ->
                if (_state.value.isLast) finish()
                else _state.update { it.copy(page = it.page + 1) }
            OnboardingIntent.Skip -> finish()
        }
    }

    private fun finish() {
        saveOnboardingCompleted()
        _effects.trySend(OnboardingEffect.NavigateToAuth)
    }
}
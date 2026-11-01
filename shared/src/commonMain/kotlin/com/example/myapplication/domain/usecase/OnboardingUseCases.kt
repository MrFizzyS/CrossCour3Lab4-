package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.OnboardingRepository

class IsOnboardingCompletedUseCase(private val repository: OnboardingRepository) {
    operator fun invoke(): Boolean = repository.isCompleted()
}

class SaveOnboardingCompletedUseCase(private val repository: OnboardingRepository) {
    operator fun invoke() = repository.setCompleted(true)
}

class ResetOnboardingUseCase(private val repository: OnboardingRepository) {
    operator fun invoke() = repository.setCompleted(false)
}
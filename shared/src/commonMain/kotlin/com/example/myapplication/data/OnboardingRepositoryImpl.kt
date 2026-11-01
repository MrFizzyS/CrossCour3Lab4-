package com.example.myapplication.data

import com.example.myapplication.domain.OnboardingRepository
import com.russhwolf.settings.Settings

class OnboardingRepositoryImpl(private val settings: Settings) : OnboardingRepository {
    override fun isCompleted(): Boolean = settings.getBoolean(KEY, false)
    override fun setCompleted(value: Boolean) = settings.putBoolean(KEY, value)

    private companion object { const val KEY = "onboarding_completed" }
}
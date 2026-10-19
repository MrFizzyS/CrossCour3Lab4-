package com.example.myapplication.data

import com.russhwolf.settings.Settings

class AppSettings(private val settings: Settings = Settings()) {

    var onboardingCompleted: Boolean
        get() = settings.getBoolean(KEY_ONBOARDING, false)
        set(value) = settings.putBoolean(KEY_ONBOARDING, value)

    var darkTheme: Boolean
        get() = settings.getBoolean(KEY_DARK, false)
        set(value) = settings.putBoolean(KEY_DARK, value)

    private companion object {
        const val KEY_ONBOARDING = "onboarding_completed"
        const val KEY_DARK = "dark_theme"
    }
}
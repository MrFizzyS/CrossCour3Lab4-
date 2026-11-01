package com.example.myapplication.ui.onboarding

data class OnboardingState(val page: Int = 0, val pageCount: Int = 3) {
    val isLast: Boolean get() = page == pageCount - 1
}

sealed interface OnboardingIntent {
    data class PageChanged(val page: Int) : OnboardingIntent
    data object Next : OnboardingIntent
    data object Skip : OnboardingIntent
}

sealed interface OnboardingEffect {
    data object NavigateToAuth : OnboardingEffect
}
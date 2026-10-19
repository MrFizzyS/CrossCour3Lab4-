package com.example.myapplication

import androidx.compose.runtime.*
import co.touchlab.kermit.Logger
import com.example.myapplication.data.AppSettings
import com.example.myapplication.ui.onboarding.OnboardingScreen
import com.example.myapplication.ui.theme.AppTheme
import com.example.myapplication.ui.todos.TodoScreen

@Composable
fun App() {
    val settings = remember { AppSettings() }
    var onboardingDone by remember { mutableStateOf(settings.onboardingCompleted) }
    var darkTheme by remember { mutableStateOf(settings.darkTheme) }

    LaunchedEffect(Unit) {
        Logger.i("App") { "App started, onboardingDone=$onboardingDone, dark=$darkTheme" }
    }

    AppTheme(darkTheme = darkTheme) {
        if (!onboardingDone) {
            OnboardingScreen(onFinish = {
                settings.onboardingCompleted = true
                onboardingDone = true
            })
        } else {
            TodoScreen(
                darkTheme = darkTheme,
                onDarkThemeChange = {
                    darkTheme = it
                    settings.darkTheme = it
                },
                onShowOnboarding = {
                    Logger.i("App") { "Onboarding reopened from Test button" }
                    settings.onboardingCompleted = false
                    onboardingDone = false
                }
            )
        }
    }
}
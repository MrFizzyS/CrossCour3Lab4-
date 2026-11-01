package com.example.myapplication

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.data.AppSettings
import com.example.myapplication.di.appModule
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.navigation.AuthRoute
import com.example.myapplication.navigation.OnboardingRoute
import com.example.myapplication.ui.main.MainViewModel
import com.example.myapplication.ui.theme.AppTheme
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    KoinApplication(application = { modules(appModule) }) {
        val appSettings = koinInject<AppSettings>()
        val mainViewModel = koinViewModel<MainViewModel>()
        val mainState by mainViewModel.state.collectAsStateWithLifecycle()
        var darkTheme by remember { mutableStateOf(appSettings.darkTheme) }

        AppTheme(darkTheme = darkTheme) {
            AppNavigation(
                startRoute = if (mainState.onboardingCompleted) AuthRoute else OnboardingRoute,
                darkTheme = darkTheme,
                onDarkThemeChange = {
                    darkTheme = it
                    appSettings.darkTheme = it
                },
                onTestOnboarding = mainViewModel::onTestOnboarding
            )
        }
    }
}
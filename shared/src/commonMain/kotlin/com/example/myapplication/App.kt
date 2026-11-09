package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.data.AppSettings
import com.example.myapplication.di.appModule
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.navigation.AuthRoute
import com.example.myapplication.navigation.HomeRoute
import com.example.myapplication.navigation.OnboardingRoute
import com.example.myapplication.ui.main.MainViewModel
import com.example.myapplication.ui.theme.AppTheme
import com.example.myapplication.ui.theme.LocalPaper
import eu.anifantakis.lib.ksafe.KSafe
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

@Composable
fun App(ksafe: KSafe? = null) {
    val secureStore = remember(ksafe) { ksafe ?: createPlatformKSafe() }

    KoinApplication(
        configuration = koinConfiguration(declaration = {
            modules(appModule, module { single { secureStore } })
        }),
        content = {
            val appSettings = koinInject<AppSettings>()
            val mainViewModel = koinViewModel<MainViewModel>()
            val mainState by mainViewModel.state.collectAsStateWithLifecycle()
            var darkTheme by remember { mutableStateOf(appSettings.darkTheme) }

            AppTheme(darkTheme = darkTheme) {
                val isAuthorized = mainState.isAuthorized
                if (isAuthorized == null) {
                    // Перевірка ще триває: порожній «стіл» замість миготіння екрана входу
                    Box(Modifier.fillMaxSize().background(LocalPaper.current.desk))
                } else {
                    val startRoute = when {
                        !mainState.onboardingCompleted -> OnboardingRoute
                        isAuthorized -> HomeRoute
                        else -> AuthRoute
                    }
                    AppNavigation(
                        startRoute = startRoute,
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
    )
}
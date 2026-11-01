package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.myapplication.ui.auth.AuthScreen
import com.example.myapplication.ui.onboarding.OnboardingScreen
import com.example.myapplication.ui.todos.TodoScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

// Потрібно для iOS і web, де немає reflection-серіалізації
private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(OnboardingRoute::class, OnboardingRoute.serializer())
            subclass(AuthRoute::class, AuthRoute.serializer())
            subclass(TodosRoute::class, TodosRoute.serializer())
        }
    }
}

@Composable
fun AppNavigation(
    startRoute: NavKey,
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    onTestOnboarding: () -> Unit
) {
    val backStack = rememberNavBackStack(navConfig, startRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<OnboardingRoute> {
                OnboardingScreen(onFinished = { backStack.replaceWith(AuthRoute) })
            }
            entry<AuthRoute> {
                AuthScreen(onAuthorized = { backStack.replaceWith(TodosRoute) })
            }
            entry<TodosRoute> {
                TodoScreen(
                    darkTheme = darkTheme,
                    onDarkThemeChange = onDarkThemeChange,
                    onShowOnboarding = {
                        onTestOnboarding()
                        backStack.replaceWith(OnboardingRoute)
                    }
                )
            }
        }
    )
}

// Додає новий екран і прибирає решту, щоб «Назад» не повертав на онбординг
private fun MutableList<NavKey>.replaceWith(key: NavKey) {
    add(key)
    while (size > 1) removeAt(0)
}
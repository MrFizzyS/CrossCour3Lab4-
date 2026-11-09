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
import com.example.myapplication.ui.home.HomeScreen
import com.example.myapplication.ui.onboarding.OnboardingScreen
import com.example.myapplication.ui.todos.TodoScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(OnboardingRoute::class, OnboardingRoute.serializer())
            subclass(AuthRoute::class, AuthRoute.serializer())
            subclass(HomeRoute::class, HomeRoute.serializer())
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
                // Успішна авторизація веде на домашній екран
                AuthScreen(onAuthorized = { backStack.replaceWith(HomeRoute) })
            }
            entry<HomeRoute> {
                HomeScreen(
                    onOpenTodos = { backStack.add(TodosRoute) },
                    onSignedOut = { backStack.replaceWith(AuthRoute) }
                )
            }
            entry<TodosRoute> {
                TodoScreen(
                    darkTheme = darkTheme,
                    onDarkThemeChange = onDarkThemeChange,
                    onShowOnboarding = {
                        onTestOnboarding()
                        backStack.replaceWith(OnboardingRoute)
                    },
                    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() }
                )
            }
        }
    )
}

private fun MutableList<NavKey>.replaceWith(key: NavKey) {
    add(key)
    while (size > 1) removeAt(0)
}
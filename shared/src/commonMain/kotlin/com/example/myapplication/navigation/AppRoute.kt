package com.example.myapplication.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable sealed interface AppRoute : NavKey
@Serializable data object OnboardingRoute : AppRoute
@Serializable data object AuthRoute : AppRoute
@Serializable data object TodosRoute : AppRoute
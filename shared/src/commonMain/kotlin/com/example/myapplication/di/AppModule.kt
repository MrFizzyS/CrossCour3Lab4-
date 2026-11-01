package com.example.myapplication.di

import com.example.myapplication.data.AppSettings
import com.example.myapplication.data.OnboardingRepositoryImpl
import com.example.myapplication.data.TodoApi
import com.example.myapplication.domain.OnboardingRepository
import com.example.myapplication.domain.usecase.*
import com.example.myapplication.ui.auth.AuthViewModel
import com.example.myapplication.ui.main.MainViewModel
import com.example.myapplication.ui.onboarding.OnboardingViewModel
import com.example.myapplication.ui.todos.TodoViewModel
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Settings> { Settings() }
    single { AppSettings(get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get()) }
    single { TodoApi() }

    factory { IsOnboardingCompletedUseCase(get()) }
    factory { SaveOnboardingCompletedUseCase(get()) }
    factory { ResetOnboardingUseCase(get()) }
    factory { ValidateUsernameUseCase() }
    factory { ValidatePasswordUseCase() }

    viewModel { MainViewModel(get(), get()) }
    viewModel { OnboardingViewModel(get()) }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { TodoViewModel(get()) }
}
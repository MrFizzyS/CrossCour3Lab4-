package com.example.myapplication.di

import com.example.myapplication.data.AppSettings
import com.example.myapplication.data.AuthRepositoryImpl
import com.example.myapplication.data.KSafeTokenStorage
import com.example.myapplication.data.OnboardingRepositoryImpl
import com.example.myapplication.data.TodoApi
import com.example.myapplication.data.remote.AuthApi
import com.example.myapplication.data.remote.createAuthClient
import com.example.myapplication.data.remote.createPlainClient
import com.example.myapplication.domain.AuthRepository
import com.example.myapplication.domain.OnboardingRepository
import com.example.myapplication.domain.TokenStorage
import com.example.myapplication.domain.usecase.*
import com.example.myapplication.ui.auth.AuthViewModel
import com.example.myapplication.ui.home.HomeViewModel
import com.example.myapplication.ui.main.MainViewModel
import com.example.myapplication.ui.onboarding.OnboardingViewModel
import com.example.myapplication.ui.todos.TodoViewModel
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<Settings> { Settings() }
    single { AppSettings(get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get()) }
    single { TodoApi() }

    single<TokenStorage> { KSafeTokenStorage(get()) }
    single(named("plain")) { createPlainClient() }
    single(named("auth")) { createAuthClient(get()) }
    single { AuthApi(get(named("plain")), get(named("auth"))) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    factory { IsOnboardingCompletedUseCase(get()) }
    factory { SaveOnboardingCompletedUseCase(get()) }
    factory { ResetOnboardingUseCase(get()) }
    factory { ValidateUsernameUseCase() }
    factory { ValidatePasswordUseCase() }
    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { IsUserAuthorizedUseCase(get()) }
    factory { GetProfileUseCase(get()) }

    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { OnboardingViewModel(get()) }
    viewModel { AuthViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { TodoViewModel(get()) }
}
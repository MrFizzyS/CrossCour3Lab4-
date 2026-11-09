package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.AuthRepository
import com.example.myapplication.domain.model.UserProfile

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): Result<Unit> =
        repository.login(username, password)
}

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}

class IsUserAuthorizedUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Boolean = repository.isAuthorized()
}

class GetProfileUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<UserProfile> = repository.getProfile()
}
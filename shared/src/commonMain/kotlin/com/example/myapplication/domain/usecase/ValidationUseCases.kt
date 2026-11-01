package com.example.myapplication.domain.usecase

enum class FieldError { EMPTY, TOO_SHORT, INVALID_CHARS, WEAK_PASSWORD }

class ValidateUsernameUseCase {
    operator fun invoke(value: String): FieldError? {
        val v = value.trim()
        return when {
            v.isEmpty() -> FieldError.EMPTY
            v.length < 3 -> FieldError.TOO_SHORT
            !v.all { it.isLetterOrDigit() || it == '_' || it == '.' } -> FieldError.INVALID_CHARS
            else -> null
        }
    }
}

class ValidatePasswordUseCase {
    operator fun invoke(value: String): FieldError? = when {
        value.isEmpty() -> FieldError.EMPTY
        value.length < 6 -> FieldError.TOO_SHORT
        !(value.any { it.isLetter() } && value.any { it.isDigit() }) -> FieldError.WEAK_PASSWORD
        else -> null
    }
}
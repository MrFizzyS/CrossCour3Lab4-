package com.example.myapplication.domain

interface OnboardingRepository {
    fun isCompleted(): Boolean
    fun setCompleted(value: Boolean)
}
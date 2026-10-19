package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val BrandPurple = Color(0xFF6C4DF6)
val BrandPurpleDark = Color(0xFF4A2FD0)
val BrandTeal = Color(0xFF14B8A6)
val BrandTealLight = Color(0xFF5EEAD4)

val BrandGradient = Brush.linearGradient(
    listOf(Color(0xFF8B6CFF), BrandPurpleDark)
)

val SoftGradient = Brush.linearGradient(
    listOf(Color(0xFFB9A8FF), BrandTeal)
)
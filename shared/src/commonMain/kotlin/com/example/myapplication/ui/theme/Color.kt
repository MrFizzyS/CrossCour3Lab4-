package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.staticCompositionLocalOf

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

data class PaperColors(
    val desk: Color,
    val paper: Color,
    val ink: Color,
    val rule: Color,
    val margin: Color
)

val LightPaper = PaperColors(
    desk = Color(0xFFD8CCB8),
    paper = Color(0xFFFBF7EC),
    ink = Color(0xFF26252E),
    rule = Color(0xFFBFD0E6),
    margin = Color(0xFFC4473A)
)

val DarkPaper = PaperColors(
    desk = Color(0xFF1F1B17),
    paper = Color(0xFF37322A),
    ink = Color(0xFFEDE6D6),
    rule = Color(0xFF5A6573),
    margin = Color(0xFFE07A70)
)

val LocalPaper = staticCompositionLocalOf { LightPaper }
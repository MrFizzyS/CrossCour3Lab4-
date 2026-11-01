package com.example.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.CompositionLocalProvider
import com.example.myapplication.generated.resources.Res
import com.example.myapplication.generated.resources.montserrat_bold
import com.example.myapplication.generated.resources.montserrat_regular
import org.jetbrains.compose.resources.Font

private val LightColors = lightColorScheme(
    primary = BrandPurple,
    onPrimary = Color.White,
    secondary = BrandTeal,
    onSecondary = Color.White,
    background = Color(0xFFF7F5FF),
    onBackground = Color(0xFF1B1B1F),
    surface = Color.White,
    onSurface = Color(0xFF1B1B1F),
    surfaceVariant = Color(0xFFEDE9FE)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB9A8FF),
    onPrimary = Color(0xFF2A1A7A),
    secondary = BrandTealLight,
    onSecondary = Color(0xFF00201C),
    background = Color(0xFF121016),
    onBackground = Color(0xFFE6E1E9),
    surface = Color(0xFF1C1B20),
    onSurface = Color(0xFFE6E1E9),
    surfaceVariant = Color(0xFF2B2833)
)

@Composable
private fun appTypography(): Typography {
    val family = FontFamily(
        Font(Res.font.montserrat_regular, FontWeight.Normal),
        Font(Res.font.montserrat_bold, FontWeight.Bold)
    )
    val base = Typography()
    return base.copy(
        headlineMedium = base.headlineMedium.copy(fontFamily = family, fontWeight = FontWeight.Bold),
        titleLarge = base.titleLarge.copy(fontFamily = family, fontWeight = FontWeight.Bold),
        bodyLarge = base.bodyLarge.copy(fontFamily = family),
        bodyMedium = base.bodyMedium.copy(fontFamily = family),
        labelLarge = base.labelLarge.copy(fontFamily = family, fontWeight = FontWeight.Bold)
    )
}


@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalPaper provides if (darkTheme) DarkPaper else LightPaper) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColors else LightColors,
            typography = appTypography(),
            shapes = Shapes(
                small = RoundedCornerShape(12.dp),
                medium = RoundedCornerShape(20.dp),
                large = RoundedCornerShape(28.dp)
            ),
            content = content
        )
    }
}
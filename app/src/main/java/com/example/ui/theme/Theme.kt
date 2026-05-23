package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = AuroraPurple,
    tertiary = AuroraBurgundy,
    background = DarkBackgroundBase,
    surface = DarkSurfaceBase,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xF0FFFFFF),
    onSurface = Color(0xF0FFFFFF)
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = AuroraPurple,
    tertiary = AuroraSoftYellow,
    background = LightBackgroundBase,
    surface = LightSurfaceBase,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Color(0xFF1C1C1E),
    onSurface = Color(0xFF1C1C1E)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // default to Dark AI Mode as requested
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

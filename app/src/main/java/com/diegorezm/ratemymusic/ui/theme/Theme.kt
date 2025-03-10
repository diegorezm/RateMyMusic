package com.diegorezm.ratemymusic.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NavyBlue,
    secondary = SeaBlue,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    background = Beige,
    onBackground = Color.Black,

    primary = NavyBlue,
    onPrimary = Beige,

    secondary = SeaBlue,
    onSecondary = Color.Black,

    tertiary = Pink40,
    onTertiary = Color.White,

    surface = Color.Black,
    onSurface = Color(0xFF1C1B1F),

    error = Red,
    onError = Color.Black
)

@Composable
fun RateMyMusicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

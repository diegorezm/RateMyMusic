package com.diegorezm.ratemymusic.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF232b36),
    onBackground = Color(0xFFc4def8),

    primary = Color(0xFF7856a8),
    onPrimary = Color(0xFFc4def8),

    secondary = Color(0xFF3d2b6c),
    onSecondary = Color(0xFFc4def8),

    tertiary = Color(0xFF241a33),
    onTertiary = Color(0xFFc4def8),

    surface = Color(0xFF232b36),
    onSurface = Color(0xFFbcabd4),

    surfaceTint = Color(0xFF85A3B2),
    outline = Color(0xFFE9D8C8),

    error = Red,
    onError = Color.Black
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

    // val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content,
        shapes = Shapes(
            small = RoundedCornerShape(2.dp),
            medium = RoundedCornerShape(4.dp),
            large = RoundedCornerShape(8.dp),
            extraLarge = RoundedCornerShape(16.dp)
        )
    )
}

@Preview(showBackground = true)
@Composable
fun DarkColorSchemePreview() {
    RateMyMusicTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Dark Color Scheme Preview",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            ColorBox(
                "Background",
                MaterialTheme.colorScheme.background,
                MaterialTheme.colorScheme.onBackground
            )
            ColorBox(
                "Primary",
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.onPrimary
            )
            ColorBox(
                "Secondary",
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.onSecondary
            )
            ColorBox(
                "Tertiary",
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.onTertiary
            )
            ColorBox(
                "Surface",
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.onSurface
            )
            ColorBox("Error", MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.onError)
        }
    }
}


@Composable
fun ColorBox(label: String, color: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 4.dp)
            .background(color, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

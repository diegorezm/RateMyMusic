package com.diegorezm.ratemymusic.core.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class ThemedCardVariant {
    Primary,
    Secondary,
    Tertiary,
    Ghost,
    Outlined
}

@Composable
fun ThemedCard(
    modifier: Modifier = Modifier,
    variant: ThemedCardVariant = ThemedCardVariant.Primary,
    content: @Composable () -> Unit = {}
) {
    val containerColor: Color
    val contentColor: Color
    val border = when (variant) {
        ThemedCardVariant.Outlined -> CardDefaults.outlinedCardBorder()
        else -> null
    }
    val elevation =
        if (variant == ThemedCardVariant.Ghost) CardDefaults.cardElevation(0.dp) else CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )

    when (variant) {
        ThemedCardVariant.Primary -> {
            containerColor = MaterialTheme.colorScheme.primary
            contentColor = MaterialTheme.colorScheme.onPrimary
        }

        ThemedCardVariant.Secondary -> {
            containerColor = MaterialTheme.colorScheme.secondary
            contentColor = MaterialTheme.colorScheme.onSecondary
        }

        ThemedCardVariant.Tertiary -> {
            containerColor = MaterialTheme.colorScheme.tertiary
            contentColor = MaterialTheme.colorScheme.onTertiary
        }

        ThemedCardVariant.Ghost -> {
            containerColor = Color.Transparent
            contentColor = MaterialTheme.colorScheme.onBackground
        }

        ThemedCardVariant.Outlined -> {
            containerColor = Color.Transparent
            contentColor = MaterialTheme.colorScheme.onBackground
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = elevation,
        border = border
    ) {
        content()
    }
}

@Preview(name = "ThemedCard")
@Composable
private fun PreviewThemedCard() {
    ThemedCard()
}
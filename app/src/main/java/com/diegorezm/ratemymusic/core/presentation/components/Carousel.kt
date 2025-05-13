package com.diegorezm.ratemymusic.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.core.presentation.theme.RateMyMusicTheme

data class CarouselItem(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val description: String? = null,
)

enum class CarouselVariant {
    Primary,
    Secondary,
    Tertiary,
    Ghost,
    Outlined
}

@Composable
fun HorizontalCarousel(
    items: List<CarouselItem>,
    variant: CarouselVariant = CarouselVariant.Primary,
    onClick: (String) -> Unit = {}
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(items) { item ->
            CarouselItem(item, variant = variant, onClick = onClick)
        }
    }
}

@Composable
fun CarouselItem(
    item: CarouselItem,
    variant: CarouselVariant = CarouselVariant.Primary,
    onClick: (String) -> Unit = {}
) {
    val containerColor: Color
    val contentColor: Color
    val surfaceColor: Color
    val border = when (variant) {
        CarouselVariant.Outlined -> CardDefaults.outlinedCardBorder()
        else -> null
    }

    when (variant) {
        CarouselVariant.Primary -> {
            containerColor = MaterialTheme.colorScheme.primary
            contentColor = MaterialTheme.colorScheme.onPrimary
            surfaceColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
        }

        CarouselVariant.Secondary -> {
            containerColor = MaterialTheme.colorScheme.secondary
            contentColor = MaterialTheme.colorScheme.onSecondary
            surfaceColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
        }

        CarouselVariant.Tertiary -> {
            containerColor = MaterialTheme.colorScheme.tertiary
            contentColor = MaterialTheme.colorScheme.onTertiary
            surfaceColor = MaterialTheme.colorScheme.onSurface
        }

        CarouselVariant.Ghost -> {
            containerColor = Color.Transparent
            contentColor = MaterialTheme.colorScheme.onBackground
            surfaceColor = MaterialTheme.colorScheme.onSurface
        }

        CarouselVariant.Outlined -> {
            containerColor = Color.Transparent
            contentColor = MaterialTheme.colorScheme.onBackground
            surfaceColor = MaterialTheme.colorScheme.onSurface
        }
    }

    Card(
        modifier = Modifier
            .width(200.dp)
            .height(250.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onClick(item.id)
            },
        colors = CardColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = if (variant == CarouselVariant.Ghost || variant == CarouselVariant.Outlined) CardDefaults.cardElevation(
            0.dp
        ) else CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.medium,
        border = border
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (item.imageUrl != null) {
                AsyncImage(
                    model = item.imageUrl,
                    placeholder = painterResource(R.drawable.undraw_happy_music),
                    fallback = painterResource(R.drawable.undraw_happy_music),
                    contentDescription = item.name,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp),
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (item.description != null) {
                Text(
                    text = item.description,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = surfaceColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun CarouselItemPrimaryPreview() {
    val item = CarouselItem(
        id = "primary",
        name = "Primary Item",
        imageUrl = "https://picsum.photos/200",
        description = "Primary style description"
    )
    RateMyMusicTheme {
        CarouselItem(item = item, variant = CarouselVariant.Primary)
    }
}

@Preview(showBackground = true)
@Composable
private fun CarouselItemSecondaryPreview() {
    val item = CarouselItem(
        id = "secondary",
        name = "Secondary Item",
        imageUrl = "https://picsum.photos/200",
        description = "Secondary style description"
    )
    RateMyMusicTheme {
        CarouselItem(item = item, variant = CarouselVariant.Secondary)
    }
}

@Preview(showBackground = true)
@Composable
private fun CarouselItemTertiaryPreview() {
    val item = CarouselItem(
        id = "tertiary",
        name = "Tertiary Item",
        imageUrl = "https://picsum.photos/200",
        description = "Tertiary style description"
    )
    RateMyMusicTheme {
        CarouselItem(item = item, variant = CarouselVariant.Tertiary)
    }
}

@Preview(showBackground = true)
@Composable
private fun CarouselItemGhostPreview() {
    val item = CarouselItem(
        id = "ghost",
        name = "Ghost Item",
        imageUrl = "https://picsum.photos/200",
        description = "Ghost style description"
    )
    RateMyMusicTheme {
        CarouselItem(item = item, variant = CarouselVariant.Ghost)
    }
}

@Preview(showBackground = true)
@Composable
private fun CarouselItemOutlinedPreview() {
    val item = CarouselItem(
        id = "outlined",
        name = "Outlined Item",
        imageUrl = "https://picsum.photos/200",
        description = "Outlined style description"
    )
    RateMyMusicTheme {
        CarouselItem(item = item, variant = CarouselVariant.Outlined)
    }
}
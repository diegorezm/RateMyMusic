package com.diegorezm.ratemymusic.user_favorites.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.core.presentation.components.CarouselItem
import com.diegorezm.ratemymusic.core.presentation.components.CarouselVariant
import com.diegorezm.ratemymusic.core.presentation.components.HorizontalCarousel
import com.diegorezm.ratemymusic.music.artists.domain.Artist


@Composable
fun UserFavoriteArtists(
    artists: List<Artist>,
    onArtistClick: (String) -> Unit
) {
    val carouselItems = artists.map {
        CarouselItem(
            id = it.id,
            name = it.name,
            imageUrl = it.imageURL ?: "",
            description = it.genres.joinToString(", ")
        )
    }

    Text(
        text = stringResource(R.string.favorite_artists),
        style = MaterialTheme.typography.titleMedium
    )

    HorizontalCarousel(carouselItems, variant = CarouselVariant.Outlined, onClick = {
        onArtistClick(it)
    })
}
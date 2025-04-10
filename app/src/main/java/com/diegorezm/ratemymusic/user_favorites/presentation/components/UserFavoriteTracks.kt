package com.diegorezm.ratemymusic.user_favorites.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.core.presentation.components.CarouselItem
import com.diegorezm.ratemymusic.core.presentation.components.CarouselVariant
import com.diegorezm.ratemymusic.core.presentation.components.HorizontalCarousel
import com.diegorezm.ratemymusic.music.tracks.domain.Track


@Composable
fun UserFavoriteTracks(
    tracks: List<Track>,
    onTrackClick: (String) -> Unit
) {
    val carouselItems = tracks.map {
        CarouselItem(
            id = it.id,
            name = it.name,
            imageUrl = it.albumCoverURL ?: "",
            description = it.artists.joinToString(", ") { it.name }
        )
    }

    Text(
        text = stringResource(R.string.favorite_tracks),
        style = MaterialTheme.typography.titleMedium
    )

    HorizontalCarousel(carouselItems, variant = CarouselVariant.Secondary, onClick = {
        onTrackClick(it)
    })
}
package com.diegorezm.ratemymusic.music.tracks.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.music.tracks.presentation.TrackScreenActions

@Composable
fun TrackActions(onAction: (TrackScreenActions) -> Unit, isFavorite: Boolean) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                if (!isFavorite) {
                    onAction(TrackScreenActions.OnAddToFavoritesClick)
                } else {
                    onAction(TrackScreenActions.OnRemoveFromFavoritesClick)
                }
            },
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Like",
                modifier = Modifier.size(30.dp),
                tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
        IconButton(
            onClick = {
                onAction(TrackScreenActions.OnOpenReviewsDrawer)

            },
        ) {
            Image(
                painter = painterResource(R.drawable.ic_chat_24),
                contentDescription = "See reviews",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}
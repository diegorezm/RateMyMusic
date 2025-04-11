package com.diegorezm.ratemymusic.music.artists.presentation

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.music.artists.domain.Artist

data class ArtistScreenState(
    val isLoading: Boolean = false,
    val error: DataError? = null,
    val artist: Artist? = null,
    val isFavorite: Boolean = false,
    val openReviewDialog: Boolean = false
)

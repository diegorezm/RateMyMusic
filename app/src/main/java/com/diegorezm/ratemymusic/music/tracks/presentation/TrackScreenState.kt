package com.diegorezm.ratemymusic.music.tracks.presentation

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.music.tracks.domain.Track

data class TrackScreenState(
    val isLoading: Boolean = false,
    val track: Track? = null,
    val error: DataError? = null,
    val isFavorite: Boolean = false,
    val openReviewDialog: Boolean = false
)

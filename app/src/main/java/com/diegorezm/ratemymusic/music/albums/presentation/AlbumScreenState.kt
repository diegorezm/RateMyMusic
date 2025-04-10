package com.diegorezm.ratemymusic.music.albums.presentation

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.music.albums.domain.Album

data class AlbumScreenState(
    val isLoading: Boolean = false,
    val error: DataError? = null,
    val album: Album? = null,
    val isFavorite: Boolean = false,
    val openReviewDialog: Boolean = false
)

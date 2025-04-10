package com.diegorezm.ratemymusic.user_favorites.presentation

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.music.albums.domain.Album
import com.diegorezm.ratemymusic.music.artists.domain.Artist
import com.diegorezm.ratemymusic.music.tracks.domain.Track

data class UserFavoritesState(
    val isLoading: Boolean = false,
    val error: DataError? = null,
    val albums: List<Album> = emptyList(),
    val artists: List<Artist> = emptyList(),
    val tracks: List<Track> = emptyList()
)

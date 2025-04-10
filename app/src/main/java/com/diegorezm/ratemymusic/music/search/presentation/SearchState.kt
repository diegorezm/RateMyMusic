package com.diegorezm.ratemymusic.music.search.presentation

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.music.albums.domain.Album
import com.diegorezm.ratemymusic.music.artists.domain.Artist
import com.diegorezm.ratemymusic.music.tracks.domain.Track

data class SearchState(
    val query: String = "",
    val searchingBy: SearchType = SearchType.TRACK,
    val albums: List<Album> = emptyList(),
    val tracks: List<Track> = emptyList(),
    val artists: List<Artist> = emptyList(),
    val isLoading: Boolean = false,
    val error: DataError? = null
)

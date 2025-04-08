package com.diegorezm.ratemymusic.music.albums.domain

import com.diegorezm.ratemymusic.music.artists.domain.Artist
import com.diegorezm.ratemymusic.music.tracks.domain.Track

data class Album(
    val id: String,
    val name: String,
    val externalUrl: String,
    val releaseDate: String,
    val genres: List<String>,
    val label: String,
    val totalTracks: Int,
    val imageURL: String?,
    val tracks: List<Track>,
    val artists: List<Artist>,
    val popularity: Int,
)
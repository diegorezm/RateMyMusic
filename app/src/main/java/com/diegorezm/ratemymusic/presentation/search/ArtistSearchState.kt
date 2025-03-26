package com.diegorezm.ratemymusic.presentation.search

import com.diegorezm.ratemymusic.modules.music.domain.models.Artist

sealed class ArtistSearchState {
    object idle : ArtistSearchState()
    object loading : ArtistSearchState()
    data class success(val artists: List<Artist>) : ArtistSearchState()
    data class error(val message: String) : ArtistSearchState()
}
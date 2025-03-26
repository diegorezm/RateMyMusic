package com.diegorezm.ratemymusic.presentation.search

import com.diegorezm.ratemymusic.modules.music.domain.models.Track

sealed class TrackSearchState {
    object Idle : TrackSearchState()
    object Loading : TrackSearchState()
    data class Success(val tracks: List<Track>) : TrackSearchState()
    data class Error(val message: String) : TrackSearchState()
}
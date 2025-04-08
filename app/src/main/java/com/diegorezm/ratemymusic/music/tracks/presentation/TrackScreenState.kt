package com.diegorezm.ratemymusic.music.tracks.presentation

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.music.tracks.domain.Track

sealed class TrackScreenState {
    data object Idle : TrackScreenState()
    data object Loading : TrackScreenState()
    data class Success(val track: Track) : TrackScreenState()
    data class Error(val error: DataError) : TrackScreenState()
}

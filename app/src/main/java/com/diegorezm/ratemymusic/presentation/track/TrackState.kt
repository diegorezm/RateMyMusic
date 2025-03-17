package com.diegorezm.ratemymusic.presentation.track

import com.diegorezm.ratemymusic.modules.music.domain.models.Track

sealed class TrackState {
    object Idle : TrackState()
    object Loading : TrackState()
    data class Success(val track: Track?) : TrackState()
    data class Error(val message: String) : TrackState()
}
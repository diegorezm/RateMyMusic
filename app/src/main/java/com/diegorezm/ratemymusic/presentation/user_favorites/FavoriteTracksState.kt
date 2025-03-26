package com.diegorezm.ratemymusic.presentation.user_favorites

import com.diegorezm.ratemymusic.modules.music.domain.models.Track

sealed class FavoriteTracksState {
    object Idle : FavoriteTracksState()
    object Loading : FavoriteTracksState()
    data class Success(val tracks: List<Track>) : FavoriteTracksState()
    data class Error(val message: String) : FavoriteTracksState()
}
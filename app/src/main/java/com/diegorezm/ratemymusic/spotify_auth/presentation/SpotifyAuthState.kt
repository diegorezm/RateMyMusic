package com.diegorezm.ratemymusic.spotify_auth.presentation

import com.diegorezm.ratemymusic.core.domain.DataError

sealed class SpotifyAuthState {
    object Idle : SpotifyAuthState()
    object Loading : SpotifyAuthState()
    object Success : SpotifyAuthState()
    data class Error(val error: DataError) : SpotifyAuthState()
}

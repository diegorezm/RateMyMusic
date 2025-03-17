package com.diegorezm.ratemymusic.presentation.track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.TracksRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.domain.use_cases.getValidSpotifyAccessTokenUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TrackViewModel(
    private val trackId: String,
    private val spotifyTokenRepository: SpotifyTokenRepository,
    private val trackRepository: TracksRepository
) : ViewModel() {
    private val _trackState = MutableStateFlow<TrackState>(TrackState.Idle)
    val trackState: StateFlow<TrackState> = _trackState

    init {
        fetchTrackData()
    }

    fun fetchTrackData() {
        viewModelScope.launch(Dispatchers.IO) {
            getValidSpotifyAccessTokenUseCase(spotifyTokenRepository).onSuccess {
                val result = trackRepository.getById(trackId, it)
                result.onSuccess {
                    _trackState.value = TrackState.Success(it)
                }.onFailure {
                    _trackState.value = TrackState.Error(it.message ?: "Unknown error")
                }

            }.onFailure {
                _trackState.value = TrackState.Error("Failed to retrieve Spotify access token")
            }
        }
    }
}
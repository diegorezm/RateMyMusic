package com.diegorezm.ratemymusic.presentation.album

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.AlbumsRepository
import com.diegorezm.ratemymusic.modules.music.domain.repositories.TracksRemoteRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.domain.use_cases.getValidSpotifyAccessTokenUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlbumViewModel(
    private val albumsRepository: AlbumsRepository,
    private val spotifyTokenRepository: SpotifyTokenRepository,
    private val albumId: String
) : ViewModel() {
    private val _albumState = MutableStateFlow<AlbumState>(AlbumState.Idle)
    val albumState: StateFlow<AlbumState> = _albumState

    init {
        fetchAlbumData()
    }

    fun fetchTrackData(context: Context, trackId: String) {
        val trackRepository = TracksRemoteRepository(context)
        viewModelScope.launch(Dispatchers.IO) {
            getValidSpotifyAccessTokenUseCase(spotifyTokenRepository).onSuccess { token ->
                val track = trackRepository.getById(trackId, token)
                Log.i("AlbumViewModel", "Track: $track")
            }
                .onFailure {
                    Log.e("AlbumViewModel", "Failed to retrieve Spotify access token", it)
                    _albumState.value =
                        AlbumState.Error("Failed to retrieve Spotify access token")
                    return@launch
                }

        }
    }

    fun fetchAlbumData() {
        viewModelScope.launch(Dispatchers.IO) {
            _albumState.value = AlbumState.Loading
            try {
                getValidSpotifyAccessTokenUseCase(spotifyTokenRepository).onSuccess { token ->
                    val result = albumsRepository.getById(albumId, token)

                    result.onSuccess {
                        _albumState.value = AlbumState.Success(it)
                    }.onFailure {
                        _albumState.value =
                            AlbumState.Error(it.message ?: "Unknown error")
                    }
                }
                    .onFailure {
                        Log.e("AlbumViewModel", "Failed to retrieve Spotify access token", it)
                        _albumState.value =
                            AlbumState.Error("Failed to retrieve Spotify access token")
                        return@launch
                    }


            } catch (e: Exception) {
                Log.e("AlbumViewModel", "An unexpected error occurred", e)
                _albumState.value =
                    AlbumState.Error("An unexpected error occurred")
            }
        }
    }
}
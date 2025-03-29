package com.diegorezm.ratemymusic.presentation.track

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteDTO
import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteType
import com.diegorezm.ratemymusic.modules.favorites.data.repositories.FavoritesRepository
import com.diegorezm.ratemymusic.modules.favorites.domain.use_cases.addToFavoritesUseCase
import com.diegorezm.ratemymusic.modules.favorites.domain.use_cases.checkIfFavoriteUseCase
import com.diegorezm.ratemymusic.modules.favorites.domain.use_cases.removeFromFavoritesUseCase
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.TracksRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.domain.use_cases.getValidSpotifyAccessTokenUseCase
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrackViewModel(
    private val trackId: String,
    private val spotifyTokenRepository: SpotifyTokenRepository,
    private val trackRepository: TracksRepository,
    private val favoritesRepository: FavoritesRepository,
    private val auth: Auth
) : ViewModel() {
    private val _trackState = MutableStateFlow<TrackState>(TrackState.Idle)
    val trackState: StateFlow<TrackState> = _trackState.onStart {
        fetchTrackData()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), TrackState.Idle)

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite =
        _isFavorite.onStart {
            checkIfFavorite()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)

    private var _favoriteId = -1

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

    fun removeFromFavorites() {
        viewModelScope.launch {
            if (_favoriteId == -1 || isFavorite.value == false) return@launch

            removeFromFavoritesUseCase(
                _favoriteId,
                favoritesRepository
            ).onSuccess {
                _isFavorite.value = false
            }.onFailure {
                Log.e("TrackViewModel", "Failed to remove track from favorites", it)
            }
        }
    }

    fun checkIfFavorite() {
        viewModelScope.launch {
            val user = auth.currentUserOrNull() ?: return@launch
            checkIfFavoriteUseCase(
                user.id,
                trackId,
                favoritesRepository

            ).onSuccess {
                if (it != null) {
                    _favoriteId = it.id
                    _isFavorite.value = true
                }
            }.onFailure {
                Log.e("TrackViewModel", "Failed to check if track with id $trackId is favorite", it)
            }

        }
    }

    fun addToFavorite() {
        viewModelScope.launch {
            val user = auth.currentUserOrNull() ?: return@launch
            val dto =
                FavoriteDTO(entityId = trackId, type = FavoriteType.TRACK, uid = user.id)
            addToFavoritesUseCase(dto, favoritesRepository).onSuccess {
                _isFavorite.value = true
            }.onFailure {
                Log.e("TrackViewModel", "Failed to add track with id $trackId to favorites", it)
            }
        }

    }

}
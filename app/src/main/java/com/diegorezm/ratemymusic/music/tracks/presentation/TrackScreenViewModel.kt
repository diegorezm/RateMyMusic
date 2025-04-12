package com.diegorezm.ratemymusic.music.tracks.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.diegorezm.ratemymusic.app.Route
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.music.tracks.domain.TracksRepository
import com.diegorezm.ratemymusic.user_favorites.data.dto.FavoriteTypeDTO
import com.diegorezm.ratemymusic.user_favorites.domain.UserFavoritesRepository
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrackScreenViewModel(
    private val trackRepository: TracksRepository,
    private val userFavoritesRepository: UserFavoritesRepository,
    auth: Auth,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val currentUserId: String = auth.currentUserOrNull()?.id ?: ""
    val trackId = savedStateHandle.toRoute<Route.TrackDetails>().trackId

    private val _state = MutableStateFlow<TrackScreenState>(TrackScreenState())
    val state = _state.onStart {
        fetchTrack()
        fetchIsFavorite()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)


    fun onAction(action: TrackScreenActions) {
        when (action) {
            is TrackScreenActions.OnAddToFavoritesClick -> {
                _state.value = _state.value.copy(isFavorite = true)
                addToFavorites()

            }

            is TrackScreenActions.OnRemoveFromFavoritesClick -> {
                _state.value = _state.value.copy(isFavorite = false)
                removeFromFavorites()
            }

            is TrackScreenActions.OnCloseReviewsDrawer -> {
                _state.value = _state.value.copy(openReviewDialog = false)
            }

            is TrackScreenActions.OnOpenReviewsDrawer -> {
                _state.value = _state.value.copy(openReviewDialog = true)
            }

            else -> Unit
        }
    }

    fun fetchTrack() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            trackRepository.getTrackById(trackId).onSuccess {
                _state.value = _state.value.copy(isLoading = false, track = it)
            }.onError {
                _state.value = _state.value.copy(isLoading = false, error = it)
            }
        }
    }


    private fun addToFavorites() {

        viewModelScope.launch {
            userFavoritesRepository.create(currentUserId, trackId, FavoriteTypeDTO.TRACK)
                .onSuccess {
                    Log.d("TrackViewModel", "addToFavorites: Success")
                }.onError {
                    _state.value = _state.value.copy(error = it)
                }
        }
    }

    private fun removeFromFavorites() {
        viewModelScope.launch {
            userFavoritesRepository.remove(currentUserId, trackId, FavoriteTypeDTO.TRACK)
                .onSuccess {
                    Log.d("TrackViewModel", "removeFromFavorites: Success")
                }.onError {
                    _state.value = _state.value.copy(error = it)
                }
        }
    }

    private fun fetchIsFavorite() {
        viewModelScope.launch {
            userFavoritesRepository.checkIfFavorite(
                currentUserId,
                trackId,
                FavoriteTypeDTO.TRACK
            ).collectLatest { isFavorite ->
                _state.update {
                    it.copy(isFavorite = isFavorite)
                }
            }
        }
    }
}
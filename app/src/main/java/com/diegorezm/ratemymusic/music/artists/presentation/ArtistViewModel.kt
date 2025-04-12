package com.diegorezm.ratemymusic.music.artists.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.diegorezm.ratemymusic.app.Route
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.music.artists.domain.ArtistRepository
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

class ArtistViewModel(
    private val artistRepository: ArtistRepository,
    private val userFavoritesRepository: UserFavoritesRepository,
    auth: Auth,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val currentUserId: String = auth.currentUserOrNull()?.id ?: ""
    private val artistId = savedStateHandle.toRoute<Route.ArtistDetails>().artistId
    private val _state = MutableStateFlow<ArtistScreenState>(ArtistScreenState())
    val state = _state.onStart {
        fetchArtist()
        fetchIsFavorite()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    fun onAction(action: ArtistScreenActions) {
        when (action) {
            is ArtistScreenActions.OnAddToFavoritesClick -> {
                _state.value = _state.value.copy(isFavorite = true)
                addToFavorites()
            }

            is ArtistScreenActions.OnRemoveFromFavoritesClick -> {
                _state.value = _state.value.copy(isFavorite = false)
                removeFromFavorites()
            }

            is ArtistScreenActions.OnCloseReviewsDrawer -> {
                _state.value = _state.value.copy(openReviewDialog = false)

            }

            is ArtistScreenActions.OnOpenReviewsDrawer -> {
                _state.value = _state.value.copy(openReviewDialog = true)
            }

            else -> Unit
        }
    }

    private fun addToFavorites() {
        viewModelScope.launch {
            userFavoritesRepository.create(currentUserId, artistId, FavoriteTypeDTO.ARTIST)
                .onSuccess {
                    Log.d("ArtistViewModel", "addToFavorites: Success")
                }.onError {
                    _state.value = _state.value.copy(error = it)
                }
        }
    }

    private fun removeFromFavorites() {
        viewModelScope.launch {
            userFavoritesRepository.remove(currentUserId, artistId, FavoriteTypeDTO.ARTIST)
                .onSuccess {
                    Log.d("ArtistViewModel", "removeFromFavorites: Success")
                }.onError {
                    _state.value = _state.value.copy(error = it)
                }
        }
    }

    private fun fetchArtist() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            artistRepository.getArtistById(artistId).onSuccess {
                _state.value = _state.value.copy(artist = it, isLoading = false)
            }.onError {
                _state.value = _state.value.copy(error = it, isLoading = false)
            }
        }
    }

    private fun fetchIsFavorite() {
        viewModelScope.launch {
            userFavoritesRepository.checkIfFavorite(
                currentUserId,
                artistId,
                FavoriteTypeDTO.ARTIST
            ).collectLatest { isFavorite ->
                Log.d("ArtistViewModel", "fetchIsFavorite: $isFavorite")
                _state.update {
                    it.copy(isFavorite = isFavorite)
                }
            }
        }

    }
}
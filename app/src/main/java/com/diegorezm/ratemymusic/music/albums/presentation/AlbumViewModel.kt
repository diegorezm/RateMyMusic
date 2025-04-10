package com.diegorezm.ratemymusic.music.albums.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.diegorezm.ratemymusic.app.Route
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.music.albums.domain.AlbumsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlbumViewModel(
    private val albumsRepository: AlbumsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val albumId = savedStateHandle.toRoute<Route.AlbumDetails>().albumId

    private val _state = MutableStateFlow<AlbumScreenState>(AlbumScreenState())
    val state =
        _state.onStart { fetchAlbum() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)


    fun onAction(action: AlbumScreenActions) {
        when (action) {
            is AlbumScreenActions.OnAddToFavoritesClick -> {
                _state.value = _state.value.copy(isFavorite = true)
            }

            is AlbumScreenActions.OnCloseReviewsDrawer -> {
                _state.value = _state.value.copy(openReviewDialog = false)
            }

            is AlbumScreenActions.OnOpenReviewsDrawer -> {
                _state.value = _state.value.copy(openReviewDialog = true)
            }

            is AlbumScreenActions.OnRemoveFromFavoritesClick -> {
                _state.value = _state.value.copy(isFavorite = false)
            }

            else -> Unit
        }
    }

    fun fetchAlbum() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            albumsRepository.getAlbumById(albumId).onSuccess {
                _state.value = _state.value.copy(album = it, isLoading = false)
            }.onError {
                _state.value = _state.value.copy(error = it, isLoading = false)
            }
        }
    }
}
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlbumViewModel(
    private val albumsRepository: AlbumsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val albumId = savedStateHandle.toRoute<Route.AlbumDetails>().albumId

    private val _state = MutableStateFlow<AlbumScreenState>(AlbumScreenState.Idle)
    val state =
        _state.onStart { fetchAlbum() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    private val _openReviewDrawer = MutableStateFlow(false)
    val openReviewDrawer = _openReviewDrawer.asStateFlow()


    fun onAction(action: AlbumScreenActions) {
        when (action) {
            is AlbumScreenActions.OnTrackClick -> Unit
            is AlbumScreenActions.OnBackClick -> Unit

            is AlbumScreenActions.OnAddToFavoritesClick -> Unit
            is AlbumScreenActions.OnCloseReviewsDrawer -> Unit

            is AlbumScreenActions.OnOpenReviewsDrawer -> {
                _openReviewDrawer.value = true
            }

            is AlbumScreenActions.OnRemoveFromFavoritesClick -> {
                _openReviewDrawer.value = false
            }

        }
    }

    fun fetchAlbum() {
        viewModelScope.launch {
            albumsRepository.getAlbumById(albumId).onSuccess {
                _state.value = AlbumScreenState.Success(it)
            }.onError {
                _state.value = AlbumScreenState.Error(it)
            }
        }
    }
}
package com.diegorezm.ratemymusic.user_favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.music.albums.domain.AlbumsRepository
import com.diegorezm.ratemymusic.music.artists.domain.ArtistRepository
import com.diegorezm.ratemymusic.music.tracks.domain.TracksRepository
import com.diegorezm.ratemymusic.user_favorites.domain.UserFavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserFavoritesViewModel(
    private val userFavoritesRepository: UserFavoritesRepository,
    private val albumRepository: AlbumsRepository,
    private val trackRepository: TracksRepository,
    private val artistRepository: ArtistRepository,
    val profileId: String
) : ViewModel() {
    private val _state = MutableStateFlow(UserFavoritesState())
    val state = _state
        .onStart {
            fetchUserFavorites()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: UserFavoritesScreenActions) {
        // just in case i need to add more actions, but for now this is useless
        when (action) {
            else -> Unit
        }
    }

    fun fetchUserFavorites() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            userFavoritesRepository.getUserFavorites(profileId).onSuccess {
                _state.value = _state.value.copy(isLoading = false)
                if (it.tracks.isNotEmpty()) {
                    fetchTracks(it.tracks.map { it.entityId })
                }

                if (it.albums.isNotEmpty()) {
                    fetchAlbums(it.albums.map { it.entityId })
                }
                if (it.artists.isNotEmpty()) {
                    fetchArtists(it.artists.map { it.entityId })
                }
            }.onError {
                _state.value = _state.value.copy(isLoading = false, error = it)
            }
        }

    }

    fun fetchAlbums(ids: List<String>) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            albumRepository.getAlbumsByIds(ids).onSuccess {
                _state.value = _state.value.copy(isLoading = false, albums = it)
            }.onError {
                _state.value = _state.value.copy(isLoading = false, error = it)
            }
        }
    }

    fun fetchTracks(ids: List<String>) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            trackRepository.getTracksByIds(ids).onSuccess {
                _state.value = _state.value.copy(isLoading = false, tracks = it)
            }
                .onError {
                    _state.value = _state.value.copy(isLoading = false, error = it)
                }
        }
    }


    fun fetchArtists(ids: List<String>) {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            artistRepository.getArtistsByIds(ids).onSuccess {
                _state.value = _state.value.copy(isLoading = false, artists = it)
            }.onError {
                _state.value = _state.value.copy(isLoading = false, error = it)
            }
        }
    }


}
package com.diegorezm.ratemymusic.user_favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.music.albums.domain.Album
import com.diegorezm.ratemymusic.music.albums.domain.AlbumsRepository
import com.diegorezm.ratemymusic.music.artists.domain.Artist
import com.diegorezm.ratemymusic.music.artists.domain.ArtistRepository
import com.diegorezm.ratemymusic.music.tracks.domain.Track
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
    private val _cachedAlbums = MutableStateFlow<List<Album>>(emptyList())
    private val _cachedTracks = MutableStateFlow<List<Track>>(emptyList())
    private val _cachedArtists = MutableStateFlow<List<Artist>>(emptyList())
    val state = _state
        .onStart {
            fetchUserFavorites()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: UserFavoritesScreenActions) {
        when (action) {
            is UserFavoritesScreenActions.OnRefreshRequested -> {
                fetchUserFavorites(true)
            }

            else -> Unit
        }
    }

    fun fetchUserFavorites(noCache: Boolean = false) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            userFavoritesRepository.getUserFavorites(profileId).onSuccess {
                _state.value = _state.value.copy(isLoading = false)
                if (it.tracks.isNotEmpty()) {
                    fetchTracks(it.tracks.map { it.entityId }, noCache)
                }

                if (it.albums.isNotEmpty()) {
                    fetchAlbums(it.albums.map { it.entityId }, noCache)
                }
                if (it.artists.isNotEmpty()) {
                    fetchArtists(it.artists.map { it.entityId }, noCache)
                }
            }.onError {
                _state.value = _state.value.copy(isLoading = false, error = it)
            }
        }

    }

    fun fetchAlbums(ids: List<String>, noCache: Boolean = false) {
        if (noCache == false && _cachedAlbums.value.isNotEmpty()) {
            _state.value = _state.value.copy(albums = _cachedAlbums.value)
            return
        }
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            albumRepository.getAlbumsByIds(ids).onSuccess {
                _state.value = _state.value.copy(isLoading = false, albums = it)
                _cachedAlbums.value = it
            }.onError {
                _state.value = _state.value.copy(isLoading = false, error = it)
            }
        }
    }

    fun fetchTracks(ids: List<String>, noCache: Boolean = false) {
        if (noCache == false && _cachedTracks.value.isNotEmpty()) {
            _state.value = _state.value.copy(tracks = _cachedTracks.value)
            return
        }
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            trackRepository.getTracksByIds(ids).onSuccess {
                _state.value = _state.value.copy(isLoading = false, tracks = it)
                _cachedTracks.value = it
            }
                .onError {
                    _state.value = _state.value.copy(isLoading = false, error = it)
                }
        }
    }


    fun fetchArtists(ids: List<String>, noCache: Boolean = false) {
        if (noCache == false && _cachedArtists.value.isNotEmpty()) {
            _state.value = _state.value.copy(artists = _cachedArtists.value)
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            artistRepository.getArtistsByIds(ids).onSuccess {
                _state.value = _state.value.copy(isLoading = false, artists = it)
                _cachedArtists.value = it
            }.onError {
                _state.value = _state.value.copy(isLoading = false, error = it)
            }
        }
    }


}
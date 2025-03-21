package com.diegorezm.ratemymusic.presentation.user_favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.favorites.data.repositories.FavoritesRepository
import com.diegorezm.ratemymusic.modules.favorites.domain.use_cases.getUserFavoritesUseCase
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.AlbumsRepository
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.TracksRepository
import com.diegorezm.ratemymusic.modules.music.domain.models.Album
import com.diegorezm.ratemymusic.modules.music.domain.models.Artist
import com.diegorezm.ratemymusic.modules.music.domain.models.Track
import com.diegorezm.ratemymusic.modules.music.domain.use_cases.getAlbumByIdsUseCase
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.domain.use_cases.getValidSpotifyAccessTokenUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserFavoritesViewModel(
    private val userId: String? = null,
    private val favoritesRepository: FavoritesRepository,
    private val albumsRepository: AlbumsRepository,
    private val tracksRepository: TracksRepository,
    private val spotifTokenRepository: SpotifyTokenRepository
) : ViewModel() {
    private val _userFavoritesState = MutableStateFlow<UserFavoritesState>(UserFavoritesState.Idle)
    val userFavoritesState = _userFavoritesState.onStart {
        fetchUserFavorites()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        UserFavoritesState.Idle
    )

    private val _albumsState = MutableStateFlow<List<Album>>(emptyList())
    val albumsState = _albumsState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        emptyList()
    )

    private val _tracksState = MutableStateFlow<List<Track>>(emptyList())
    val tracksState = _tracksState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        emptyList()
    )

    private val _artistsState = MutableStateFlow<List<Artist>>(emptyList())
    val artistsState = _artistsState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        emptyList()
    )

    fun fetchUserFavorites() {
        val uid = getUserID() ?: return
        viewModelScope.launch {
            getUserFavoritesUseCase(uid, favoritesRepository).onSuccess {
                _userFavoritesState.value = UserFavoritesState.Success(it)
                if (it.albums.isNotEmpty()) {
                    fetchAlbums()
                }
                if (it.tracks.isNotEmpty()) {
                    fetchTracks()
                }
            }.onFailure {
                _userFavoritesState.value = UserFavoritesState.Error(it.message ?: "Unknown error")
            }
        }
    }

    private fun fetchAlbums() {
        val favorites = (userFavoritesState.value as UserFavoritesState.Success).favorites
        viewModelScope.launch(Dispatchers.IO) {
            getValidSpotifyAccessTokenUseCase(spotifTokenRepository).onSuccess {
                getAlbumByIdsUseCase(
                    favorites.albums,
                    it,
                    albumsRepository
                ).onSuccess {
                    _albumsState.value = it
                }.onFailure {
                    _userFavoritesState.value = UserFavoritesState.Error("Couldn't fetch albums.")
                }
            }.onFailure {
                _userFavoritesState.value =
                    UserFavoritesState.Error("Could not get a valid spotify token.")
            }
        }
    }

    private fun fetchTracks() {
        if (userFavoritesState.value is UserFavoritesState.Success) {
            viewModelScope.launch(Dispatchers.IO) {
                return@launch
            }
        }
    }


    private fun fetchArtists() {}

    private fun getUserID(): String? {
        val uid: String

        if (userId != null) {
            uid = userId
        } else {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) return null
            uid = user.uid
        }

        return uid
    }
}
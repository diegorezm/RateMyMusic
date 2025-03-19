package com.diegorezm.ratemymusic.presentation.album

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteDTO
import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteType
import com.diegorezm.ratemymusic.modules.favorites.domain.use_cases.addToFavoritesUseCase
import com.diegorezm.ratemymusic.modules.favorites.domain.use_cases.checkIfFavoriteUseCase
import com.diegorezm.ratemymusic.modules.favorites.domain.use_cases.removeFromFavoritesUseCase
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.AlbumsRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.domain.use_cases.getValidSpotifyAccessTokenUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlbumViewModel(
    private val albumsRepository: AlbumsRepository,
    private val spotifyTokenRepository: SpotifyTokenRepository,
    private val albumId: String
) : ViewModel() {
    private val _albumState = MutableStateFlow<AlbumState>(AlbumState.Idle)
    val albumState = _albumState.onStart {
        fetchAlbumData()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), AlbumState.Idle)

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.onStart {
        checkIfFavorite()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)


    fun removeFromFavorites() {
        viewModelScope.launch {
            val user = Firebase.auth.currentUser
            if (user == null) return@launch
            removeFromFavoritesUseCase(
                FavoriteDTO(
                    uid = user.uid,
                    type = FavoriteType.ALBUM,
                    favoriteId = albumId
                )
            ).onSuccess {
                Log.d("AlbumViewModel", "Album with id $albumId removed from favorites")
                _isFavorite.value = false
            }.onFailure {
                Log.e("AlbumViewModel", "Failed to remove album from favorites", it)
            }
        }
    }

    fun checkIfFavorite() {
        viewModelScope.launch {
            val user = Firebase.auth.currentUser
            if (user == null) return@launch

            checkIfFavoriteUseCase(
                FavoriteDTO(
                    uid = user.uid,
                    type = FavoriteType.ALBUM,
                    favoriteId = albumId
                )

            ).onSuccess {
                _isFavorite.value = it
            }.onFailure {
                Log.e("AlbumViewModel", "Failed to check if album with id $albumId is favorite", it)
            }

        }
    }

    fun addToFavorite() {
        viewModelScope.launch {
            val user = Firebase.auth.currentUser
            if (user == null) return@launch
            val dto =
                FavoriteDTO(favoriteId = albumId, type = FavoriteType.ALBUM, uid = user.uid)
            addToFavoritesUseCase(dto).onSuccess {
                Log.d("AlbumViewModel", "Album added to favorites")
                _isFavorite.value = true
            }.onFailure {
                Log.e("AlbumViewModel", "Failed to add album with id $albumId to favorites", it)
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
package com.diegorezm.ratemymusic.presentation.album

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.di.AppModule
import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteDTO
import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteType
import com.diegorezm.ratemymusic.modules.favorites.domain.use_cases.addToFavoritesUseCase
import com.diegorezm.ratemymusic.modules.favorites.domain.use_cases.checkIfFavoriteUseCase
import com.diegorezm.ratemymusic.modules.favorites.domain.use_cases.removeFromFavoritesUseCase
import com.diegorezm.ratemymusic.modules.spotify_auth.domain.use_cases.getValidSpotifyAccessTokenUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlbumViewModel(
    val appModule: AppModule,
    private val albumId: String,
) : ViewModel() {
    private val _albumState = MutableStateFlow<AlbumState>(AlbumState.Idle)
    val albumState = _albumState.onStart {
        fetchAlbumData()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), AlbumState.Idle)

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite =
        _isFavorite.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)

    private var _favoriteId: Int = -1

    init {
        checkIfFavorite()
    }

    fun removeFromFavorites() {
        viewModelScope.launch {
            val user = appModule.auth.currentUserOrNull()
            if (user == null || _favoriteId == -1 || isFavorite.value == false) return@launch
            Log.d("AlbumViewModel", "Removing album with id $_favoriteId")
            removeFromFavoritesUseCase(
                userId = user.id,
                favoriteId = _favoriteId,
                appModule.favoritesRepository
            ).onSuccess {
                _isFavorite.value = false
            }
        }
    }

    fun checkIfFavorite() {
        viewModelScope.launch {
            val user = appModule.auth.currentUserOrNull()
            if (user == null) return@launch

            checkIfFavoriteUseCase(
                userId = user.id,
                entityId = albumId,
                appModule.favoritesRepository

            ).onSuccess {
                if (it != null) {
                    _favoriteId = it.id
                    _isFavorite.value = true
                }
            }

        }
    }

    fun addToFavorite() {
        viewModelScope.launch {
            val user = appModule.auth.currentUserOrNull()
            if (user == null) return@launch
            val dto =
                FavoriteDTO(entityId = albumId, type = FavoriteType.ALBUM, uid = user.id)
            addToFavoritesUseCase(dto, appModule.favoritesRepository).onSuccess {
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
                getValidSpotifyAccessTokenUseCase(appModule.spotifyTokenRepository).onSuccess { token ->
                    val result = appModule.albumsRepository.getById(albumId, token)

                    result.onSuccess {
                        _albumState.value = AlbumState.Success(it)
                    }.onFailure {
                        Log.e("AlbumViewModel", "Failed to retrieve album", it)
                        _albumState.value =
                            AlbumState.Error("Failed to retrieve album.")
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
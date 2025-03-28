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
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewDTO
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewEntityType
import com.diegorezm.ratemymusic.modules.reviews.domain.use_cases.createReviewUseCase
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
    val appModule: AppModule,
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

    fun writeReview(review: String, albumId: String, loadReviews: () -> Unit = {}) {
        var user = Firebase.auth.currentUser
        if (user == null) return

        val reviewDTO = ReviewDTO(
            reviewerId = user.uid,
            entityId = albumId,
            entityType = ReviewEntityType.ALBUM,
            content = review,
        )

        viewModelScope.launch {
            createReviewUseCase(reviewDTO, appModule.reviewsRepository).onSuccess {
                Log.i("AlbumViewModel", "Review created")
                loadReviews()
            }.onFailure {
                Log.e("AlbumViewModel", "Failed to create review", it)
            }
        }
    }

    fun removeFromFavorites() {
        viewModelScope.launch {
            val user = Firebase.auth.currentUser
            if (user == null) return@launch
            removeFromFavoritesUseCase(
                FavoriteDTO(
                    uid = user.uid,
                    type = FavoriteType.ALBUM,
                    favoriteId = albumId
                ),
                appModule.favoritesRepository
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
                ),
                appModule.favoritesRepository

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
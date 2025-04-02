package com.diegorezm.ratemymusic.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.AlbumsRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.domain.use_cases.getValidSpotifyAccessTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val albumsRepository: AlbumsRepository,
    private val spotifyTokenRepository: SpotifyTokenRepository
) : ViewModel() {
    private val _homeState = MutableStateFlow<HomeState>(HomeState.Idle)
    val homeState = _homeState.onStart {
        fetchNewReleases()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), HomeState.Idle)

    fun fetchNewReleases() {
        viewModelScope.launch {
            getValidSpotifyAccessTokenUseCase(spotifyTokenRepository).onSuccess {
                albumsRepository.getNewReleases(10, 0, it).onSuccess {
                    _homeState.value = HomeState.Success(it)
                }
            }.onFailure {
                _homeState.value = HomeState.Error(it.message ?: "Unknown error")
            }
        }
    }

}
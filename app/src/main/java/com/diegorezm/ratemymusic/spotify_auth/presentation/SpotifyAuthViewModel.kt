package com.diegorezm.ratemymusic.spotify_auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.spotify_auth.domain.SpotifyTokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SpotifyAuthViewModel(
    private val spotifyTokenRepository: SpotifyTokenRepository
) : ViewModel() {
    private val _state = MutableStateFlow<SpotifyAuthState>(SpotifyAuthState.Idle)
    val state =
        _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SpotifyAuthState.Idle)

    fun onSaveToken(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = SpotifyAuthState.Loading
            spotifyTokenRepository.requestAccesToken(code).onSuccess {
                _state.value = SpotifyAuthState.Success
            }.onError {
                _state.value = SpotifyAuthState.Error(it)
            }

        }
    }
}
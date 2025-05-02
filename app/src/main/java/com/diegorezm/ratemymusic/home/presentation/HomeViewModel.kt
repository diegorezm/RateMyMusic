package com.diegorezm.ratemymusic.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.music.albums.domain.Album
import com.diegorezm.ratemymusic.music.albums.domain.AlbumsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val albumRepository: AlbumsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    private val _cachedAlbums = MutableStateFlow<List<Album>>(emptyList())
    val state = _state.onStart { fetchAlbums() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), HomeState.Idle)

    fun fetchAlbums(isPullRequest: Boolean = false) {
        if (isPullRequest == false && _cachedAlbums.value.isNotEmpty() && _state.value !is HomeState.Loading) {
            _state.value = HomeState.Success(_cachedAlbums.value)
            return
        }
        viewModelScope.launch {
            _state.value = HomeState.Loading
            albumRepository.getNewReleases().onSuccess {
                _state.value = HomeState.Success(it)
                _cachedAlbums.value = it
            }.onError {
                _state.value = HomeState.Error(it)
            }
        }
    }
}
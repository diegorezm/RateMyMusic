package com.diegorezm.ratemymusic.music.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.music.search.domain.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SearchState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    fun onAction(action: SearchScreenActions) {
        when (action) {
            is SearchScreenActions.OnFilterChange -> {
                _state.value = _state.value.copy(searchingBy = action.type)
                fetchByFilterType(action.type)
            }

            is SearchScreenActions.OnQueryChange -> {
                _state.value = _state.value.copy(query = action.query)
                fetchByFilterType(searchBy = _state.value.searchingBy)
            }

            else -> Unit

        }
    }

    private fun fetchByFilterType(searchBy: SearchType) {
        if (_state.value.query.isBlank()) return
        when (searchBy) {
            SearchType.ALBUM -> fetchAlbums()
            SearchType.TRACK -> fetchTracks()
            SearchType.ARTIST -> fetchArtists()
        }
    }

    private fun fetchAlbums() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            searchRepository.searchByAlbum(
                query = _state.value.query
            ).onSuccess {
                _state.value = _state.value.copy(albums = it, isLoading = false)
            }.onError {
                _state.value = _state.value.copy(error = it, isLoading = false)
            }
        }
    }

    private fun fetchTracks() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            searchRepository.searchByTrack(
                query = _state.value.query
            ).onSuccess {
                _state.value = _state.value.copy(tracks = it, isLoading = false)
            }.onError {
                _state.value = _state.value.copy(error = it, isLoading = false)
            }
        }
    }

    private fun fetchArtists() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            searchRepository.searchByArtist(
                query = _state.value.query
            ).onSuccess {
                _state.value = _state.value.copy(artists = it, isLoading = false)
            }.onError {
                _state.value = _state.value.copy(error = it, isLoading = false)
            }
        }
    }
}
package com.diegorezm.ratemymusic.music.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.music.albums.domain.Album
import com.diegorezm.ratemymusic.music.artists.domain.Artist
import com.diegorezm.ratemymusic.music.search.domain.SearchRepository
import com.diegorezm.ratemymusic.music.tracks.domain.Track
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _cachedArtists = MutableStateFlow<List<Artist>>(emptyList())
    private val _cachedAlbums = MutableStateFlow<List<Album>>(emptyList())
    private val _cachedTracks = MutableStateFlow<List<Track>>(emptyList())

    private val _query = MutableStateFlow("")

    private val _state = MutableStateFlow(SearchState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        viewModelScope.launch {
            _query.debounce(400).distinctUntilChanged().collectLatest {
                _state.value = _state.value.copy(query = it)
                fetchByFilterType(_state.value.searchingBy)
            }
        }
    }

    fun onAction(action: SearchScreenActions) {
        when (action) {
            is SearchScreenActions.OnFilterChange -> {
                _state.value = _state.value.copy(searchingBy = action.type)
                fetchByFilterType(action.type, true)
            }

            is SearchScreenActions.OnQueryChange -> {
                _state.value = _state.value.copy(query = action.query)
                _query.value = action.query
            }

            else -> Unit

        }
    }

    private fun fetchByFilterType(searchBy: SearchType, showCached: Boolean = false) {
        if (_state.value.query.isBlank()) return
        when (searchBy) {
            SearchType.ALBUM -> fetchAlbums(showCached)
            SearchType.TRACK -> fetchTracks(showCached)
            SearchType.ARTIST -> fetchArtists(showCached)
        }
    }

    private fun fetchAlbums(showCached: Boolean = false) {
        if (showCached) {
            _state.value = _state.value.copy(albums = _cachedAlbums.value)
            return
        }
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            searchRepository.searchByAlbum(
                query = _state.value.query
            ).onSuccess {
                _state.value = _state.value.copy(albums = it, isLoading = false)
                _cachedAlbums.value = it
            }.onError {
                _state.value = _state.value.copy(error = it, isLoading = false)
            }
        }
    }

    private fun fetchTracks(showCached: Boolean = false) {
        if (showCached) {
            _state.value = _state.value.copy(tracks = _cachedTracks.value)
            return
        }
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            searchRepository.searchByTrack(
                query = _state.value.query
            ).onSuccess {
                _state.value = _state.value.copy(tracks = it, isLoading = false)
                _cachedTracks.value = it
            }.onError {
                _state.value = _state.value.copy(error = it, isLoading = false)
            }
        }
    }

    private fun fetchArtists(showCached: Boolean = false) {
        if (showCached) {
            _state.value = _state.value.copy(artists = _cachedArtists.value)
            return
        }
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            searchRepository.searchByArtist(
                query = _state.value.query
            ).onSuccess {
                _state.value = _state.value.copy(artists = it, isLoading = false)
                _cachedArtists.value = it
            }.onError {
                _state.value = _state.value.copy(error = it, isLoading = false)
            }
        }
    }
}
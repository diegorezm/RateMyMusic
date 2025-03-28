package com.diegorezm.ratemymusic.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.music.data.remote.api.RemoteSearchType
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.SearchRepository
import com.diegorezm.ratemymusic.modules.music.domain.models.SearchRequest
import com.diegorezm.ratemymusic.modules.music.domain.use_cases.searchAlbumsUseCase
import com.diegorezm.ratemymusic.modules.music.domain.use_cases.searchByArtistUseCase
import com.diegorezm.ratemymusic.modules.music.domain.use_cases.searchByTrackUseCase
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.domain.use_cases.getValidSpotifyAccessTokenUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val spotifyTokenRepository: SpotifyTokenRepository
) : ViewModel() {

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchingFor = MutableStateFlow(RemoteSearchType.ALBUM)
    val searchingFor: StateFlow<RemoteSearchType> = _searchingFor.asStateFlow()

    init {
        observeSearchQuery()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSearchingFor(type: RemoteSearchType) {
        _searchingFor.value = type
        if (_searchQuery.value.isNotBlank()) search(_searchQuery.value)
        else _searchState.value = SearchState.Idle
    }


    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(600)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotBlank()) {
                        search(query)
                    } else {
                        _searchState.value = SearchState.Idle
                    }
                }
        }
    }

    private fun search(q: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getValidSpotifyAccessTokenUseCase(spotifyTokenRepository).onSuccess {
                val request = SearchRequest(
                    query = q,
                    limit = 10,
                    offset = 0,
                    spotifyAuthToken = it
                )

                _searchState.value = SearchState.Loading

                Log.i("SearchViewModel", "Searching for: $q")

                val result = when (searchingFor.value) {
                    RemoteSearchType.ALBUM -> searchAlbumsUseCase(request, searchRepository)
                    RemoteSearchType.ARTIST -> searchByArtistUseCase(request, searchRepository)
                    RemoteSearchType.TRACK -> searchByTrackUseCase(request, searchRepository)
                }

                _searchState.value = result.fold(
                    onSuccess = {
                        Log.d("SearchViewModel", "Success: $it")
                        SearchState.Success(it)
                    },
                    onFailure = {
                        Log.d("SearchViewModel", "Error while searching.", it)
                        SearchState.Error("Something went wrong.")
                    }
                )
            }.onFailure {
                Log.d("SearchViewModel", "Error: ${it.message}")
                _searchState.value = SearchState.Error("Could not get a valid spotify token.")
            }
        }
    }
}

package com.diegorezm.ratemymusic.presentation.search

import com.diegorezm.ratemymusic.modules.music.domain.models.PaginatedResult

sealed class SearchState {
    object Idle : SearchState()
    object Loading : SearchState()
    data class Success<T>(val result: PaginatedResult<T>) : SearchState()
    data class Error(val message: String) : SearchState()
}

package com.diegorezm.ratemymusic.music.tracks.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.diegorezm.ratemymusic.app.Route
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.music.tracks.domain.TracksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrackScreenViewModel(
    private val trackRepository: TracksRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val trackId = savedStateHandle.toRoute<Route.TrackDetails>().trackId

    private val _state = MutableStateFlow<TrackScreenState>(TrackScreenState.Idle)
    val state = _state.onStart {
        fetchTrack()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    private val _openReviewDrawer = MutableStateFlow(false)
    val openReviewDrawer = _openReviewDrawer.asStateFlow()


    fun onAction(action: TrackScreenActions) {
        when (action) {
            is TrackScreenActions.OnBackClick -> Unit
            is TrackScreenActions.OnAddToFavoritesClick -> Unit
            is TrackScreenActions.OnRemoveFromFavoritesClick -> TODO()

            is TrackScreenActions.OnCloseReviewsDrawer -> {
                _openReviewDrawer.value = false
            }

            is TrackScreenActions.OnOpenReviewsDrawer -> {
                _openReviewDrawer.value = true
            }

        }
    }

    fun fetchTrack() {
        viewModelScope.launch {
            trackRepository.getTrackById(trackId).onSuccess {
                _state.value = TrackScreenState.Success(it)
            }.onError {
                _state.value = TrackScreenState.Error(it)
            }
        }
    }
}
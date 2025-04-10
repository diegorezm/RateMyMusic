package com.diegorezm.ratemymusic.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.profile.domain.repositories.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val profileId: String,
    private val isCurrentUser: Boolean
) : ViewModel() {
    private val _state = MutableStateFlow<ProfileState>(ProfileState())
    val state = _state.onStart {
        fetchProfile()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        if (isCurrentUser) {
            _state.value = _state.value.copy(isCurrentUser = true)
        }
    }

    fun fetchProfile() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            profileRepository.getProfileById(profileId).onSuccess {
                _state.value = _state.value.copy(isLoading = false, profile = it)
            }.onError {
                _state.value = _state.value.copy(isLoading = false, error = it)
            }
        }
    }
}
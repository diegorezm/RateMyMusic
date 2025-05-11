package com.diegorezm.ratemymusic.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.followers.domain.FollowersRepository
import com.diegorezm.ratemymusic.profile.domain.repositories.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val followersRepository: FollowersRepository,
    private val profileId: String,
    private val currentUserId: String,
) : ViewModel() {
    private val _state =
        MutableStateFlow<ProfileState>(ProfileState())
    val state = _state.onStart {
        fetchProfile()
        fetchFollowsAndFollowers()
        observeFollows()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        _state.value = _state.value.copy(isCurrentUser = currentUserId == profileId)
    }

    fun onAction(action: ProfileScreenActions) {
        when (action) {
            is ProfileScreenActions.OnFollowClick -> {
                follow()
            }

            is ProfileScreenActions.OnUnfollowClick -> {
                unfollow()
            }

            else -> Unit
        }
    }


    fun fetchFollowsAndFollowers() {
        viewModelScope.launch {
            followersRepository.getFollowersCount(profileId).onSuccess {
                _state.value = _state.value.copy(followersCount = it)
            }.onError {
                _state.value = _state.value.copy(error = it)
            }

            if (state.value.error != null) {
                return@launch
            }

            followersRepository.getFollowingCount(profileId).onSuccess {
                _state.value = _state.value.copy(followingCount = it)
            }.onError {
                _state.value = _state.value.copy(error = it)
            }
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

    private fun observeFollows() {
        if (state.value.isCurrentUser) return
        viewModelScope.launch {
            followersRepository.isFollower(currentUserId, profileId).collectLatest {
                _state.value = _state.value.copy(isFollowing = it)
            }
        }
    }

    private fun follow() {
        if (state.value.isCurrentUser) return
        viewModelScope.launch {
            followersRepository.follow(currentUserId, profileId).onSuccess {
                _state.value = _state.value.copy(
                    isFollowing = true,
                    followersCount = state.value.followersCount + 1
                )
                fetchProfile()
            }.onError {
                _state.value = _state.value.copy(error = it)
            }
        }
    }

    private fun unfollow() {
        if (state.value.isCurrentUser) return
        viewModelScope.launch {
            followersRepository.unfollow(currentUserId, profileId).onSuccess {
                _state.value = _state.value.copy(
                    isFollowing = false,
                    followersCount = state.value.followersCount - 1
                )
                fetchProfile()
            }.onError {
                _state.value = _state.value.copy(error = it)
            }
        }
    }
}
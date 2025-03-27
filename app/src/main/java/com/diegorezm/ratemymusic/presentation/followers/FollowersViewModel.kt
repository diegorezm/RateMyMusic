package com.diegorezm.ratemymusic.presentation.followers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.followers.data.models.FollowDTO
import com.diegorezm.ratemymusic.modules.followers.data.repositories.FollowersRepository
import com.diegorezm.ratemymusic.modules.followers.domain.use_cases.followUseCase
import com.diegorezm.ratemymusic.modules.followers.domain.use_cases.getFollowersCountUseCase
import com.diegorezm.ratemymusic.modules.followers.domain.use_cases.getFollowingCountUseCase
import com.diegorezm.ratemymusic.modules.followers.domain.use_cases.isFollowingUseCase
import com.diegorezm.ratemymusic.modules.followers.domain.use_cases.unfollowUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FollowersViewModel(
    private val followersRepository: FollowersRepository,
    private val userId: String? = null
) : ViewModel() {
    private val _followersCount = MutableStateFlow(0)
    val followersCount =
        _followersCount.onStart {
            fetchFollowersCount()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0)

    private val _followingCount = MutableStateFlow(0)
    val followingCount =
        _followingCount.onStart {
            fetchFollowingCount()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0)

    private val _isFollowing = MutableStateFlow(false)
    val isFollowing =
        _isFollowing.onStart {
            fetchIsFollowing()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)

    fun follow() {
        viewModelScope.launch {
            val user = Firebase.auth.currentUser ?: return@launch
            val followingId = getUserID() ?: return@launch
            if (followingId == user.uid) return@launch

            val dto = FollowDTO(
                followerId = user.uid,
                followedId = followingId
            )

            followUseCase(followDTO = dto, followersRepository).onSuccess {
                _isFollowing.value = true
                _followersCount.value = _followersCount.value + 1
            }.onFailure {
                Log.d("FollowersViewModel", "follow: ${it.message}")
            }

        }
    }

    fun unfollow() {
        viewModelScope.launch {
            val user = Firebase.auth.currentUser ?: return@launch
            val followingId = getUserID() ?: return@launch
            if (followingId == user.uid) return@launch

            val dto = FollowDTO(
                followerId = user.uid,
                followedId = followingId
            )
            unfollowUseCase(followDTO = dto, followersRepository).onSuccess {
                _isFollowing.value = false
                _followersCount.value = _followersCount.value - 1
            }.onFailure {
                Log.d("FollowersViewModel", "unfollow: ${it.message}")
            }

        }
    }

    fun fetchIsFollowing() {
        viewModelScope.launch {
            val user = Firebase.auth.currentUser ?: return@launch
            val followingId = userId ?: return@launch
            val isFollowing = isFollowingUseCase(followingId, user.uid).getOrElse {
                false
            }
            _isFollowing.value = isFollowing
        }
    }


    fun fetchFollowersCount() {
        viewModelScope.launch {
            val uid = getUserID() ?: return@launch
            val followersCount = getFollowersCountUseCase(uid)
            followersCount.onSuccess {
                _followersCount.value = it
            }.onFailure {
                _followersCount.value = 0
            }
        }
    }

    fun fetchFollowingCount() {
        viewModelScope.launch {
            val uid = getUserID() ?: return@launch
            val followingCount = getFollowingCountUseCase(uid)
            followingCount.onSuccess {
                _followingCount.value = it
            }.onFailure {
                _followingCount.value = 0
            }
        }
    }


    private fun getUserID(): String? {
        var uid: String

        if (userId != null) {
            uid = userId
        } else {
            val user = Firebase.auth.currentUser
            if (user == null) return null
            uid = user.uid
        }

        return uid
    }
}

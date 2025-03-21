package com.diegorezm.ratemymusic.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.SignInRouteId
import com.diegorezm.ratemymusic.modules.auth.use_cases.signOutUseCase
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.domain.use_cases.getProfileUseCase
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userId: String? = null,
    private val profileRepository: ProfileRepository,
    private val spotifyTokenRepository: SpotifyTokenRepository,
) : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState = _profileState.onStart {
        fetchProfile()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ProfileState.Idle)

    fun signOut(navController: NavController) {
        viewModelScope.launch {
            signOutUseCase().onSuccess {
                spotifyTokenRepository.clearToken()
                navController.navigate(SignInRouteId)
            }.onFailure {
                _profileState.value = ProfileState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun fetchProfile() {
        val uid = getUserID() ?: return
        viewModelScope.launch {
            val profile = getProfileUseCase(uid, profileRepository)
            profile.onSuccess {
                _profileState.value = ProfileState.Success(it)
            }.onFailure {
                _profileState.value = ProfileState.Error(it.message ?: "Unknown error")
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
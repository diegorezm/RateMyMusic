package com.diegorezm.ratemymusic.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.domain.use_cases.getProfileUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

open class ProfileViewModel(
    private val userId: String? = null,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState = _profileState.onStart {
        fetchProfile()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ProfileState.Idle)


    fun fetchProfile() {
        val uid = getUserID() ?: return
        viewModelScope.launch {
            val profile = getProfileUseCase(uid, profileRepository)
            profile.onSuccess {
                _profileState.value = ProfileState.Success(it)
            }.onFailure {
                Log.e("ProfileViewModel", "Failed to retrieve profile", it)
                _profileState.value = ProfileState.Error("Failed to retrieve profile")
            }
        }
    }


    fun getUserID(): String? {
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
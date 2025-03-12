package com.diegorezm.ratemymusic.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.profiles.use_cases.getProfileUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileResult>(ProfileResult.Idle)
    val profileState: StateFlow<ProfileResult> = _profileState

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        val user = Firebase.auth.currentUser
        if (user == null) return
        viewModelScope.launch {
            val profile = getProfileUseCase(user.uid.toString())
            profile.onSuccess {
                _profileState.value = ProfileResult.Success(it)
            }.onFailure {
                _profileState.value = ProfileResult.Error(it.message ?: "Unknown error")
            }
        }
    }
}
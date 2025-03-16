package com.diegorezm.ratemymusic.presentation.profile

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.SpotifyAuthActivity
import com.diegorezm.ratemymusic.modules.profiles.data_access.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.use_cases.getProfileUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileResult>(ProfileResult.Idle)
    val profileState: StateFlow<ProfileResult> = _profileState

    init {
        fetchProfile()
    }

    fun handleSpotifyAuthBtn(context: Context) {
        val intent = Intent(context, SpotifyAuthActivity::class.java)
        context.startActivity(intent)
    }

    fun fetchProfile() {
        val user = Firebase.auth.currentUser
        if (user == null) return
        viewModelScope.launch {
            val profile = getProfileUseCase(user.uid.toString(), profileRepository)
            profile.onSuccess {
                _profileState.value = ProfileResult.Success(it)
            }.onFailure {
                _profileState.value = ProfileResult.Error(it.message ?: "Unknown error")
            }
        }
    }
}
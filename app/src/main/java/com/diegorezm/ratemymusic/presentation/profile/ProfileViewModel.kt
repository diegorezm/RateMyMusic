package com.diegorezm.ratemymusic.presentation.profile

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.SpotifyAuthActivity
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.domain.use_cases.getProfileUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState = _profileState.onStart {
        fetchProfile()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ProfileState.Idle)

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
                _profileState.value = ProfileState.Success(it)
            }.onFailure {
                _profileState.value = ProfileState.Error(it.message ?: "Unknown error")
            }
        }
    }
}
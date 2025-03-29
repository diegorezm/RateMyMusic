package com.diegorezm.ratemymusic.presentation.profile.me

import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.presentation.profile.ProfileViewModel
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.launch

class MyProfileViewModel(
    profileRepository: ProfileRepository,
    private val auth: Auth
) : ProfileViewModel(
    userId = auth.currentUserOrNull()?.id,
    profileRepository = profileRepository,
    auth
) {

    fun signout() {
        viewModelScope.launch {
            auth.signOut()
        }
    }
}
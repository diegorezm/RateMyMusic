package com.diegorezm.ratemymusic.presentation.profile.profiles

import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.presentation.profile.ProfileViewModel
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfilesViewModel(
    private val userId: String,
    private val auth: Auth,
    profileRepository: ProfileRepository,
) : ProfileViewModel(
    userId = userId,
    profileRepository = profileRepository, auth
) {
    private val _isCurrentUser = MutableStateFlow(false)
    val isCurrentUser =
        _isCurrentUser.asStateFlow()

    init {
        checkIsCurrentUser()
    }

    fun checkIsCurrentUser() {
        _isCurrentUser.value = userId == auth.currentUserOrNull()?.id
    }
}
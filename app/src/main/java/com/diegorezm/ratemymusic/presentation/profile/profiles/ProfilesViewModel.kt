package com.diegorezm.ratemymusic.presentation.profile.profiles

import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.presentation.profile.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfilesViewModel(
    private val userId: String,
    profileRepository: ProfileRepository,
) : ProfileViewModel(
    userId = userId,
    profileRepository = profileRepository
) {
    private val _isCurrentUser = MutableStateFlow(false)
    val isCurrentUser =
        _isCurrentUser.asStateFlow()

    init {
        checkIsCurrentUser()
    }

    fun checkIsCurrentUser() {
        _isCurrentUser.value = userId == Firebase.auth.currentUser?.uid
    }
}
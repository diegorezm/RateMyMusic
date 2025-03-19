package com.diegorezm.ratemymusic.presentation.profile

import com.diegorezm.ratemymusic.modules.profiles.domain.models.Profile

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(val profile: Profile?) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

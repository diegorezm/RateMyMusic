package com.diegorezm.ratemymusic.presentation.profile

import com.diegorezm.ratemymusic.modules.profiles.models.Profile

sealed class ProfileResult {
    object Idle : ProfileResult()
    object Loading : ProfileResult()
    data class Success(val profile: Profile?) : ProfileResult()
    data class Error(val message: String) : ProfileResult()
}

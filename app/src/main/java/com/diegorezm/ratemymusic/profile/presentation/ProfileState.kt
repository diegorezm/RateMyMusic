package com.diegorezm.ratemymusic.profile.presentation

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.profile.domain.models.Profile

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val error: DataError? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val isFollowing: Boolean = false,
    val isCurrentUser: Boolean = false
)
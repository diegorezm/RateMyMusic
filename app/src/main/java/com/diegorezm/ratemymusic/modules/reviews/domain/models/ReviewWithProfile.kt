package com.diegorezm.ratemymusic.modules.reviews.domain.models

import com.diegorezm.ratemymusic.modules.profiles.domain.models.Profile

data class ReviewWithProfile(
    val review: Review,
    val profile: Profile
)

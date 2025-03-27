package com.diegorezm.ratemymusic.modules.followers.domain.models

import com.google.firebase.Timestamp

data class Following(
    val profileId: String,
    val createdAt: Timestamp
) {
    constructor() : this("", Timestamp.now())
}

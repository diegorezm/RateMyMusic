package com.diegorezm.ratemymusic.modules.profiles.domain.models

data class Profile(
    val name: String,
    val email: String,
    val photoUrl: String?,
    val followersIds: List<String> = emptyList(),
) {
    constructor() : this("", "", null, emptyList())
}

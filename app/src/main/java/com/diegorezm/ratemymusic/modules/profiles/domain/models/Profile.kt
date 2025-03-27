package com.diegorezm.ratemymusic.modules.profiles.domain.models

data class Profile(
    val uid: String,
    val name: String,
    val email: String,
    val photoUrl: String?
) {
    constructor() : this("", "", "", null)
}

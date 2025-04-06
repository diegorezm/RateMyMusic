package com.diegorezm.ratemymusic.profile.domain.models

data class Profile(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val createdAt: String,
)

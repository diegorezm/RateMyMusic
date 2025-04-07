package com.diegorezm.ratemymusic.modules.music.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedDTO<T>(
    val href: String,
    val limit: Int = 10,
    val next: String?,
    val offset: Int = 0,
    val previous: String?,
    val total: Int = 0,
    val items: List<T>
) {
}
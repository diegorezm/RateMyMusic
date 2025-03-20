package com.diegorezm.ratemymusic.modules.music.domain.models

data class PaginatedResult<T>(
    val items: List<T>,
    val total: Int,
    val next: String?,
    val previous: String?
)

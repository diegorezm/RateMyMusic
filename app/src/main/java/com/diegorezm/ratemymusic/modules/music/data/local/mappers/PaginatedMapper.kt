package com.diegorezm.ratemymusic.modules.music.data.local.mappers

import com.diegorezm.ratemymusic.modules.music.data.remote.models.PaginatedDTO
import com.diegorezm.ratemymusic.modules.music.domain.models.PaginatedResult

fun <T, R> PaginatedDTO<T>.toDomain(transform: (T) -> R): PaginatedResult<R> {
    return PaginatedResult(
        items = this.items.map(transform),
        total = this.total,
        next = this.next,
        previous = this.previous
    )
}


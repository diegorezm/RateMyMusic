package com.diegorezm.ratemymusic.modules.music.data.remote.mappers

import com.diegorezm.ratemymusic.modules.music.data.remote.models.TracksDTO
import com.diegorezm.ratemymusic.modules.music.domain.models.Tracks

fun TracksDTO.toDomain(): Tracks {
    return Tracks(
        href = this.href,
        limit = this.limit,
        next = this.next,
        offset = this.offset,
        previous = this.previous,
        items = this.items.map { it.toDomain() }
    )
}
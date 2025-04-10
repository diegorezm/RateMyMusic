package com.diegorezm.ratemymusic.reviews.data.mappers

import com.diegorezm.ratemymusic.profile.data.mappers.toDomain
import com.diegorezm.ratemymusic.reviews.data.dto.ReviewWithProfileDTO
import com.diegorezm.ratemymusic.reviews.domain.Review

fun ReviewWithProfileDTO.toDomain(): Review {
    return Review(
        id = id,
        content = content,
        entityId = entityId,
        entity = entityType.toString(),
        rating = rating,
        profile = profile.toDomain(),
        createdAt = createdAt
    )
}
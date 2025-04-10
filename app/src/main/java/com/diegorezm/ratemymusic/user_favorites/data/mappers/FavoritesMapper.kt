package com.diegorezm.ratemymusic.user_favorites.data.mappers

import com.diegorezm.ratemymusic.user_favorites.data.dto.UserFavoriteDTO
import com.diegorezm.ratemymusic.user_favorites.domain.UserFavorite

fun UserFavoriteDTO.toDomain(): UserFavorite {
    return UserFavorite(
        id = this.id ?: 0,
        uid = this.uid,
        entityId = this.entityId
    )
}
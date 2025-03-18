package com.diegorezm.ratemymusic.modules.favorites.domain.use_cases

import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteDTO
import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteType
import com.diegorezm.ratemymusic.modules.favorites.data.repositories.FavoritesRepository
import com.diegorezm.ratemymusic.modules.favorites.domain.repositories.FavoritesRepositoryImpl

suspend fun removeFromFavoritesUseCase(
    favoriteDTO: FavoriteDTO,
    favoritesRepository: FavoritesRepository = FavoritesRepositoryImpl()
): Result<Unit> {
    when (favoriteDTO.type) {
        FavoriteType.TRACK -> return favoritesRepository.removeFavoriteTrack(
            favoriteDTO.uid,
            favoriteDTO.favoriteId
        )

        FavoriteType.ALBUM -> return favoritesRepository.removeFavoriteAlbum(
            favoriteDTO.uid,
            favoriteDTO.favoriteId
        )

        FavoriteType.ARTIST -> return favoritesRepository.removeFavoriteArtist(
            favoriteDTO.uid,
            favoriteDTO.favoriteId
        )
    }
}
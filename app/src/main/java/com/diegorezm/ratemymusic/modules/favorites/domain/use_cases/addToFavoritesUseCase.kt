package com.diegorezm.ratemymusic.modules.favorites.domain.use_cases

import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteDTO
import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteType
import com.diegorezm.ratemymusic.modules.favorites.data.repositories.FavoritesRepository
import com.diegorezm.ratemymusic.modules.favorites.domain.repositories.FavoritesRepositoryImpl

suspend fun addToFavoritesUseCase(
    favoriteDTO: FavoriteDTO,
    favoritesRepository: FavoritesRepository = FavoritesRepositoryImpl()
): Result<Unit> {
    when (favoriteDTO.type) {
        FavoriteType.TRACK -> return favoritesRepository.addFavoriteTrack(
            favoriteDTO.uid,
            favoriteDTO.favoriteId
        )

        FavoriteType.ALBUM -> return favoritesRepository.addFavoriteAlbum(
            favoriteDTO.uid,
            favoriteDTO.favoriteId
        )

        FavoriteType.ARTIST -> return favoritesRepository.addFavoriteArtist(
            favoriteDTO.uid,
            favoriteDTO.favoriteId
        )
    }
}
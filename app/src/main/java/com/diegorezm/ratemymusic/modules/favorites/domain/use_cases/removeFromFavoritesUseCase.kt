package com.diegorezm.ratemymusic.modules.favorites.domain.use_cases

import com.diegorezm.ratemymusic.modules.favorites.data.repositories.FavoritesRepository
import com.diegorezm.ratemymusic.utils.handleResult

suspend fun removeFromFavoritesUseCase(
    favoriteId: Int,
    favoritesRepository: FavoritesRepository
): Result<Unit> {
    // Idk if i trust the supabase policies to check stuff for me. but
    // for now that is what i will do.
    return handleResult(tag = "removeFromFavoritesUseCase") {
        favoritesRepository.remove(favoriteId)
    }
}
package com.diegorezm.ratemymusic.modules.music.domain.use_cases

import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.SearchRepository
import com.diegorezm.ratemymusic.modules.music.domain.models.AlbumSimple
import com.diegorezm.ratemymusic.modules.music.domain.models.PaginatedResult
import com.diegorezm.ratemymusic.modules.music.domain.models.SearchRequest

suspend fun searchAlbumsUseCase(
    request: SearchRequest,
    searchRepository: SearchRepository
): Result<PaginatedResult<AlbumSimple>> {
    return searchRepository.searchByAlbum(request)
}
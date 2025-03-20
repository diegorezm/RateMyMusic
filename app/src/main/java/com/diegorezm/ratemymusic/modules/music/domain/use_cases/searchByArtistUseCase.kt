package com.diegorezm.ratemymusic.modules.music.domain.use_cases

import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.SearchRepository
import com.diegorezm.ratemymusic.modules.music.domain.models.Artist
import com.diegorezm.ratemymusic.modules.music.domain.models.PaginatedResult
import com.diegorezm.ratemymusic.modules.music.domain.models.SearchRequest

suspend fun searchByArtistUseCase(
    request: SearchRequest,
    searchRepository: SearchRepository
): Result<PaginatedResult<Artist>> {
    return searchRepository.searchByArtist(request)
}
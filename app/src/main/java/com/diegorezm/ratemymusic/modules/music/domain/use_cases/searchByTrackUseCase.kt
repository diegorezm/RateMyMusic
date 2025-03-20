package com.diegorezm.ratemymusic.modules.music.domain.use_cases

import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.SearchRepository
import com.diegorezm.ratemymusic.modules.music.domain.models.PaginatedResult
import com.diegorezm.ratemymusic.modules.music.domain.models.SearchRequest
import com.diegorezm.ratemymusic.modules.music.domain.models.Track

suspend fun searchByTrackUseCase(
    request: SearchRequest,
    searchRepository: SearchRepository
): Result<PaginatedResult<Track>> {
    return searchRepository.searchByTrack(request)
}
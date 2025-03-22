package com.diegorezm.ratemymusic.modules.music.domain.repositories

import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.SearchRepository
import com.diegorezm.ratemymusic.modules.music.domain.models.AlbumSimple
import com.diegorezm.ratemymusic.modules.music.domain.models.Artist
import com.diegorezm.ratemymusic.modules.music.domain.models.PaginatedResult
import com.diegorezm.ratemymusic.modules.music.domain.models.SearchRequest
import com.diegorezm.ratemymusic.modules.music.domain.models.Track

class SearchMockRepository : SearchRepository {
    override suspend fun searchByAlbum(request: SearchRequest): Result<PaginatedResult<AlbumSimple>> {
        return Result.success(
            PaginatedResult(
                listOf(),
                total = 0,
                next = null,
                previous = null
            )
        )
    }

    override suspend fun searchByArtist(request: SearchRequest): Result<PaginatedResult<Artist>> {
        return Result.success(
            PaginatedResult(
                listOf(),
                total = 0,
                next = null,
                previous = null
            )
        )
    }

    override suspend fun searchByTrack(request: SearchRequest): Result<PaginatedResult<Track>> {
        return Result.success(
            PaginatedResult(
                listOf(),
                total = 0,
                next = null,
                previous = null
            )
        )
    }
}
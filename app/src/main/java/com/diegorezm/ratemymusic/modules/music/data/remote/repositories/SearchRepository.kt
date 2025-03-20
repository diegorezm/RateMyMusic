package com.diegorezm.ratemymusic.modules.music.data.remote.repositories

import com.diegorezm.ratemymusic.modules.music.domain.models.AlbumSimple
import com.diegorezm.ratemymusic.modules.music.domain.models.Artist
import com.diegorezm.ratemymusic.modules.music.domain.models.PaginatedResult
import com.diegorezm.ratemymusic.modules.music.domain.models.SearchRequest
import com.diegorezm.ratemymusic.modules.music.domain.models.Track

interface SearchRepository {
    suspend fun searchByAlbum(
        request: SearchRequest
    ): Result<PaginatedResult<AlbumSimple>>

    suspend fun searchByArtist(
        request: SearchRequest
    ): Result<PaginatedResult<Artist>>

    suspend fun searchByTrack(
        request: SearchRequest
    ): Result<PaginatedResult<Track>>

}
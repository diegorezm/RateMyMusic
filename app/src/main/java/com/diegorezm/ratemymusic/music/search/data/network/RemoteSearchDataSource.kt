package com.diegorezm.ratemymusic.music.search.data.network

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.music.albums.data.dto.PaginatedAlbumDTO
import com.diegorezm.ratemymusic.music.artists.data.dto.PaginatedArtistDTO
import com.diegorezm.ratemymusic.music.shared.dto.PaginationDTO
import com.diegorezm.ratemymusic.music.tracks.data.dto.PaginatedTrackDTO

interface RemoteSearchDataSource {
    suspend fun searchByAlbum(
        query: String,
        pagination: PaginationDTO = PaginationDTO()
    ): Result<PaginatedAlbumDTO, DataError.Remote>

    suspend fun searchByTrack(
        query: String,
        pagination: PaginationDTO = PaginationDTO()
    ): Result<PaginatedTrackDTO, DataError.Remote>

    suspend fun searchByArtist(
        query: String,
        pagination: PaginationDTO = PaginationDTO()
    ): Result<PaginatedArtistDTO, DataError.Remote>

}
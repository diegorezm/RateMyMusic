package com.diegorezm.ratemymusic.music.search.domain

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.music.albums.domain.Album
import com.diegorezm.ratemymusic.music.artists.domain.Artist
import com.diegorezm.ratemymusic.music.shared.dto.PaginationDTO
import com.diegorezm.ratemymusic.music.tracks.domain.Track

interface SearchRepository {
    suspend fun searchByAlbum(
        query: String,
        pagination: PaginationDTO = PaginationDTO()
    ): Result<List<Album>, DataError>

    suspend fun searchByTrack(
        query: String,
        pagination: PaginationDTO = PaginationDTO()
    ): Result<List<Track>, DataError>

    suspend fun searchByArtist(
        query: String,
        pagination: PaginationDTO = PaginationDTO()
    ): Result<List<Artist>, DataError>

}
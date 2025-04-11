package com.diegorezm.ratemymusic.music.artists.data.network

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.modules.music.data.remote.models.ArtistDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackBulkDTO
import com.diegorezm.ratemymusic.music.albums.data.dto.PaginatedAlbumDTO
import com.diegorezm.ratemymusic.music.artists.data.dto.ArtistBulkDTO
import com.diegorezm.ratemymusic.music.shared.dto.PaginationDTO

interface RemoteArtistDataSource {
    suspend fun getArtistById(id: String): Result<ArtistDTO, DataError.Remote>
    suspend fun getArtistsByIds(ids: List<String>): Result<ArtistBulkDTO, DataError.Remote>
    suspend fun getArtistTopTracks(id: String): Result<TrackBulkDTO, DataError.Remote>
    suspend fun getArtistAlbums(
        id: String,
        pagination: PaginationDTO
    ): Result<PaginatedAlbumDTO, DataError.Remote>

}
package com.diegorezm.ratemymusic.music.artists.data.network

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.modules.music.data.remote.models.ArtistDTO

interface RemoteArtistDataSource {
    suspend fun getArtistById(id: String): Result<ArtistDTO, DataError.Remote>
    suspend fun getArtistsByIds(ids: List<String>): Result<List<ArtistDTO>, DataError.Remote>
}
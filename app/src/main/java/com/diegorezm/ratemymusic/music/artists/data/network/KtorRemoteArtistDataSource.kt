package com.diegorezm.ratemymusic.music.artists.data.network

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.modules.music.data.remote.models.ArtistDTO
import com.diegorezm.ratemymusic.spotify_auth.domain.SpotifyTokenRepository
import io.ktor.client.HttpClient

class KtorRemoteArtistDataSource(
    private val httpClient: HttpClient,
    private val spotifyTokenRepository: SpotifyTokenRepository
) : RemoteArtistDataSource {
    private val url = ""
    override suspend fun getArtistById(id: String): Result<ArtistDTO, DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun getArtistsByIds(ids: List<String>): Result<List<ArtistDTO>, DataError.Remote> {
        TODO("Not yet implemented")
    }
}
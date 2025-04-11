package com.diegorezm.ratemymusic.music.artists.domain

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.music.albums.domain.Album
import com.diegorezm.ratemymusic.music.tracks.domain.Track

interface ArtistRepository {
    suspend fun getArtistById(id: String): Result<Artist, DataError>
    suspend fun getArtistsByIds(ids: List<String>): Result<List<Artist>, DataError>
    suspend fun getArtistTopTracks(id: String): Result<List<Track>, DataError>
    suspend fun getArtistAlbums(
        id: String,
        limit: Int = 10,
        offset: Int = 0
    ): Result<List<Album>, DataError>
}
package com.diegorezm.ratemymusic.modules.music.data.remote.repositories

import com.diegorezm.ratemymusic.modules.music.domain.models.Album
import kotlinx.coroutines.flow.Flow

interface ArtistsRepository {
    fun getById(id: String): Any
    fun getArtistTopTracks(id: String): Flow<List<Any>>
    fun getAlbums(id: String): Flow<List<Album>>
}
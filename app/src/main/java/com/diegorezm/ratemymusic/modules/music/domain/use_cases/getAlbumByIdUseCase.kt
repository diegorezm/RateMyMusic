package com.diegorezm.ratemymusic.modules.music.domain.use_cases

import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.AlbumsRepository
import com.diegorezm.ratemymusic.modules.music.domain.models.Album

suspend fun getAlbumByIdUseCase(
    albumId: String,
    spotifyToken: String,
    albumRepository: AlbumsRepository
): Result<Album> {
    return albumRepository.getById(albumId, spotifyToken)
}
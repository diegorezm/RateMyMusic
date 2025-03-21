package com.diegorezm.ratemymusic.modules.music.domain.use_cases

import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.AlbumsRepository
import com.diegorezm.ratemymusic.modules.music.domain.models.Album

suspend fun getAlbumByIdsUseCase(
    ids: List<String>,
    spotifyAuthToken: String,
    repository: AlbumsRepository
): Result<List<Album>> {
    return repository.getByIds(ids, spotifyAuthToken)
}
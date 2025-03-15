package com.diegorezm.ratemymusic.modules.music.data.remote.repositories

import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun getById(id: String): Any
    fun getByIds(ids: List<String>): Flow<List<Any>>
}
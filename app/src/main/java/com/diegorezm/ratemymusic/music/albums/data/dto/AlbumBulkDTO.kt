package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.google.gson.annotations.SerializedName

data class AlbumBulkDTO(
    @SerializedName("albums") val albums: List<AlbumDTO>
)

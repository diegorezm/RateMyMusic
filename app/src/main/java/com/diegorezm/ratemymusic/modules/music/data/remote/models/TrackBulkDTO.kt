package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.google.gson.annotations.SerializedName

data class TrackBulkDTO(
    @SerializedName("tracks") val tracks: List<TrackDTO>
)

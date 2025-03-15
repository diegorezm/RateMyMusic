package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.google.gson.annotations.SerializedName

data class TrackDTO(
    @SerializedName("artists") val artists: List<ArtistDTO>,
    @SerializedName("available_markets") val availableMarkets: List<String>,
    @SerializedName("disc_number") val discNumber: Int,
    @SerializedName("duration_ms") val durationMs: Int,
    @SerializedName("explicit") val explicit: Boolean,
    @SerializedName("external_urls") val externalUrls: ExternalUrlsDTO,
    @SerializedName("href") val href: String,
    @SerializedName("id") val id: String,
    @SerializedName("is_playable") val isPlayable: Boolean,
    @SerializedName("linked_from") val linkedFrom: LinkedFromDTO?,
    @SerializedName("restrictions") val restrictions: RestrictionsDTO?,
    @SerializedName("name") val name: String,
    @SerializedName("preview_url") val previewUrl: String?,
    @SerializedName("track_number") val trackNumber: Int,
    @SerializedName("type") val type: String,
    @SerializedName("uri") val uri: String,
    @SerializedName("is_local") val isLocal: Boolean
)

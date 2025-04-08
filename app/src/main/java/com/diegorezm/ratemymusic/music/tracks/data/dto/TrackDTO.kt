package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.diegorezm.ratemymusic.music.albums.data.dto.AlbumDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackDTO(
    @SerialName("artists") val artists: List<ArtistDTO>,
    @SerialName("available_markets") val availableMarkets: List<String>,
    @SerialName("disc_number") val discNumber: Int,
    @SerialName("duration_ms") val durationMs: Int,
    @SerialName("explicit") val explicit: Boolean,
    @SerialName("external_urls") val externalUrls: ExternalUrlsDTO,
    @SerialName("href") val href: String,
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("track_number") val trackNumber: Int,
    @SerialName("type") val type: String,
    @SerialName("uri") val uri: String,
    @SerialName("is_local") val isLocal: Boolean,
    
    @SerialName("preview_url") val previewUrl: String? = null,
    @SerialName("linked_from") val linkedFrom: LinkedFromDTO? = null,
    @SerialName("restrictions") val restrictions: RestrictionsDTO? = null,
    @SerialName("is_playable") val isPlayable: Boolean = false,
    @SerialName("album") val album: AlbumDTO? = null
)


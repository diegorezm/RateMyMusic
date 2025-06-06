package com.diegorezm.ratemymusic.music.albums.data.dto

import com.diegorezm.ratemymusic.modules.music.data.remote.models.ArtistDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.CopyrightDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.ExternalIdsDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.ExternalUrlsDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.ImageDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.PaginatedDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.RestrictionsDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumDTO(
    @SerialName("id") val id: String,
    @SerialName("album_type") val albumType: String,
    @SerialName("total_tracks") val totalTracks: Int,
    @SerialName("available_markets") val availableMarkets: List<String>,
    @SerialName("external_urls") val externalUrls: ExternalUrlsDTO,
    @SerialName("href") val href: String,
    @SerialName("images") val images: List<ImageDTO>,
    @SerialName("name") val name: String,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("release_date_precision") val releaseDatePrecision: String,
    @SerialName("type") val type: String,
    @SerialName("uri") val uri: String,
    @SerialName("artists") val artists: List<ArtistDTO>,
    @SerialName("restrictions") val restrictions: RestrictionsDTO? = null,
    @SerialName("copyrights") val copyrights: List<CopyrightDTO>? = null,
    @SerialName("external_ids") val externalIds: ExternalIdsDTO? = null,
    @SerialName("tracks") val tracks: PaginatedDTO<TrackDTO>? = null,
    @SerialName("genres") val genres: List<String>? = null,
    @SerialName("label") val label: String? = null,
    @SerialName("popularity") val popularity: Int? = null
)


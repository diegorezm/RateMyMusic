package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.google.gson.annotations.SerializedName

data class AlbumDTO(
    @SerializedName("album_type") val albumType: String,
    @SerializedName("total_tracks") val totalTracks: Int,
    @SerializedName("available_markets") val availableMarkets: List<String>,
    @SerializedName("external_urls") val externalUrls: ExternalUrlsDTO,
    @SerializedName("href") val href: String,
    @SerializedName("id") val id: String,
    @SerializedName("images") val images: List<ImageDTO>,
    @SerializedName("name") val name: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("release_date_precision") val releaseDatePrecision: String,
    @SerializedName("restrictions") val restrictions: RestrictionsDTO?,
    @SerializedName("type") val type: String,
    @SerializedName("uri") val uri: String,
    @SerializedName("artists") val artists: List<ArtistSimpleDTO> = emptyList(),
    @SerializedName("tracks") val tracks: PaginatedDTO<TrackSimpleDTO>,
    @SerializedName("copyrights") val copyrights: List<CopyrightDTO>,
    @SerializedName("external_ids") val externalIds: ExternalIdsDTO,
    @SerializedName("genres") val genres: List<String>,
    @SerializedName("label") val label: String,
    @SerializedName("popularity") val popularity: Int
)

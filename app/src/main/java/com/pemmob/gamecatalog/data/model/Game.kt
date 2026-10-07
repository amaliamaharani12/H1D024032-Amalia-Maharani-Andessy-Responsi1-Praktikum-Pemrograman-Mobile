package com.pemmob.gamecatalog.data.model

import com.google.gson.annotations.SerializedName

data class GameListResponse(
    val count: Int,
    val results: List<Game>
)

data class Game(
    val id: Int,
    val name: String,
    val released: String?,
    val rating: Double,
    @SerializedName("background_image") val backgroundImage: String?
)

data class Genre(
    val id: Int,
    val name: String
)

data class GameDetail(
    val id: Int,
    val name: String,
    val released: String?,
    val rating: Double,
    @SerializedName("description_raw") val descriptionRaw: String?,
    @SerializedName("background_image") val backgroundImage: String?,
    val metacritic: Int?,
    val genres: List<Genre>?
)
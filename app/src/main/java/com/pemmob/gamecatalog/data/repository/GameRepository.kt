package com.pemmob.gamecatalog.data.repository

import com.pemmob.gamecatalog.BuildConfig
import com.pemmob.gamecatalog.data.model.Game
import com.pemmob.gamecatalog.data.model.GameDetail
import com.pemmob.gamecatalog.network.ApiClient
import com.pemmob.gamecatalog.network.RawgApi

class GameRepository(
    private val api: RawgApi = ApiClient.instance
) {
    suspend fun getGames(query: String): List<Game> =
        api.getGames(
            apiKey = BuildConfig.RAWG_API_KEY,
            search = query.ifBlank { null }
        ).results

    suspend fun getGameDetail(id: Int): GameDetail =
        api.getGameDetail(id = id, apiKey = BuildConfig.RAWG_API_KEY)
}
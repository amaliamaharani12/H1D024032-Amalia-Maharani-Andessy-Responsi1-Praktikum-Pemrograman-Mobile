package com.pemmob.gamecatalog.network

import com.pemmob.gamecatalog.data.model.GameDetail
import com.pemmob.gamecatalog.data.model.GameListResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgApi {

    @GET("games")
    suspend fun getGames(
        @Query("key") apiKey: String,
        @Query("search") search: String? = null,
        @Query("page_size") pageSize: Int = 20
    ): GameListResponse

    @GET("games/{id}")
    suspend fun getGameDetail(
        @Path("id") id: Int,
        @Query("key") apiKey: String
    ): GameDetail
}

object ApiClient {
    private const val BASE_URL = "https://api.rawg.io/api/"

    val instance: RawgApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RawgApi::class.java)
    }
}
package com.example.nbademo.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit rozhraní definující dostupné endpointy API balldontlie.io.
 * * Všechny požadavky vyžadují autorizační hlavičku, která je automaticky
 * přidávána pomocí [okhttp3.OkHttpClient] interceptoru.
 */
interface NbaApi {
    companion object {
        const val BASE_URL = "https://api.balldontlie.io/v1/"
    }

    /**
     * Získá stránkovaný seznam hráčů NBA.
     *
     * @param cursor ID posledního prvku z předchozího požadavku pro navigaci na další stránku.
     * @param perPage Počet záznamů na jednu stránku (výchozí hodnota 35).
     * @return [PlayerListResponse] obsahující seznam DTO objektů a metadata.
     */
    @GET("players")
    suspend fun getPlayers(
        @Query("cursor") cursor: Int? = null,
        @Query("per_page") perPage: Int = 35
    ): PlayerListResponse

    /**
     * Získá detail konkrétního hráče.
     *
     * @param playerId Unikátní identifikátor hráče v systému API.
     * @return [PlayerDetailResponse] s daty hráče.
     */
    @GET("players/{id}")
    suspend fun getPlayerDetail(
        @Path("id") playerId: Int
    ): PlayerDetailResponse

    /**
     * Získá detail konkrétního týmu.
     *
     * @param teamId Unikátní identifikátor týmu v systému API.
     * @return [TeamDetailResponse] s daty týmu.
     */
    @GET("teams/{id}")
    suspend fun getTeamDetail(
        @Path("id") teamId: Int
    ): TeamDetailResponse
}
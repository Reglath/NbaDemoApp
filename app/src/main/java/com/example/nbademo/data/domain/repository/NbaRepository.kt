package com.example.nbademo.data.domain.repository

import com.example.nbademo.data.domain.model.Player
import com.example.nbademo.data.domain.model.Team

/**
 * Rozhraní definující metody pro získávání dat o NBA hráčích a týmech.
 * Slouží jako abstrakce mezi datovými zdroji (API) a zbytkem aplikace (ViewModel).
 */
interface NbaRepository {
    /**
     * Načte stránkovaný seznam hráčů.
     * * @param cursor Identifikátor pro stránkování (pozice v databázi API).
     * @return [Result] obsahující dvojici: seznam [Player] a volitelný cursor pro další stránku.
     */
    suspend fun getPlayers(cursor: Int?): Result<Pair<List<Player>, Int?>>

    /**
     * Získá detailní informace o konkrétním hráči podle jeho ID.
     * * @param id Unikátní ID hráče.
     * @return [Result] s objektem [Player] nebo chybou.
     */
    suspend fun getPlayerById(id: Int): Result<Player>

    /**
     * Získá detailní informace o konkrétním týmu podle jeho ID.
     * * @param id Unikátní ID týmu.
     * @return [Result] s objektem [Team] nebo chybou.
     */
    suspend fun getTeamById(id: Int): Result<Team>
}
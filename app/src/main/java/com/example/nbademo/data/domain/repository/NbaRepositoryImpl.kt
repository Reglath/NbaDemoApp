package com.example.nbademo.data.domain.repository

import com.example.nbademo.data.domain.model.Player
import com.example.nbademo.data.domain.model.Team
import com.example.nbademo.data.remote.NbaApi
import com.example.nbademo.data.remote.dto.toDomain
import javax.inject.Inject

/**
 * Implementace rozhraní [NbaRepository].
 */
class NbaRepositoryImpl @Inject constructor(
    private val api: NbaApi
) : NbaRepository {

    override suspend fun getPlayers(cursor: Int?): Result<Pair<List<Player>, Int?>> {
        return try {
            val response = api.getPlayers(cursor = cursor)

            val domainPlayers = response.data.map { it.toDomain() }

            val nextCursor = response.meta.nextCursor

            Result.success(domainPlayers to nextCursor)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPlayerById(id: Int): Result<Player> {
        return try {
            val response = api.getPlayerDetail(id)
            Result.success(response.data.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTeamById(id: Int): Result<Team> {
        return try {
            val response = api.getTeamDetail(id)
            Result.success(response.data.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
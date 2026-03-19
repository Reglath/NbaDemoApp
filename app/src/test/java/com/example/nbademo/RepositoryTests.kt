package com.example.nbademo

import com.example.nbademo.data.domain.repository.NbaRepositoryImpl
import com.example.nbademo.data.remote.NbaApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Test

class NbaRepositoryTest {

    private val api = mockk<NbaApi>()
    private val repository = NbaRepositoryImpl(api)

    @Test
    fun getPlayersShouldReturnFailureIfException() = runTest {
        coEvery { api.getPlayers(any(), any()) } throws Exception("HTTP 429 Too Many Requests")

        val result = repository.getPlayers(null)

        assertTrue(result.isFailure)
        assertEquals("HTTP 429 Too Many Requests", result.exceptionOrNull()?.message)
    }

    @Test
    fun getTeamShouldReturnFailureIfTeamDoesntExist() = runTest {
        coEvery { api.getTeamDetail(any()) } throws Exception("Not Found")

        val result = repository.getTeamById(999)

        assertTrue(result.isFailure)
    }
}
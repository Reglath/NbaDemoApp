package com.example.nbademo

import com.example.nbademo.data.remote.NbaApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.Test

class ApiTests {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: NbaApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NbaApi::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getPlayersShouldParseCorrectly() = runBlocking {
        val mockJsonResponse = """
            {
              "data": [
                { "id": 1, "first_name": "LeBron", "last_name": "James" }
              ],
              "meta": { "next_cursor": 2, "per_page": 35 }
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(mockJsonResponse).setResponseCode(200))

        val response = api.getPlayers()

        assertEquals(1, response.data.size)
        assertEquals("LeBron", response.data[0].firstName)
        assertEquals(2, response.meta.nextCursor)
    }
}
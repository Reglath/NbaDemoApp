package com.example.nbademo

import com.example.nbademo.di.NetworkModule
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkTests {

    @Test
    fun headerIsAddedCorrectly() {
        val server = MockWebServer()
        server.enqueue(MockResponse())

        val client: OkHttpClient = NetworkModule.provideOkHttpClient()

        val request = Request.Builder().url(server.url("/test")).build()
        client.newCall(request).execute()

        val recordedRequest = server.takeRequest()
        val authHeader = recordedRequest.getHeader("Authorization")

        assert(authHeader != null)
        assertEquals(BuildConfig.NBA_API_KEY, authHeader)

        server.shutdown()
    }
}
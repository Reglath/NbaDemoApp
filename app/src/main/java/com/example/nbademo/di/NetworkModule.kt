package com.example.nbademo.di

import com.example.nbademo.BuildConfig
import com.example.nbademo.data.remote.NbaApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dagger Hilt modul pro poskytování instancí souvisejících se síťovou komunikací.
 * * Modul je instalován do [SingletonComponent], aby se instance
 * [OkHttpClient] a [NbaApi] sdílely v rámci celé aplikace.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", BuildConfig.NBA_API_KEY)
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideNbaApi(okHttpClient: OkHttpClient): NbaApi {
        return Retrofit.Builder()
            .baseUrl(NbaApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NbaApi::class.java)
    }
}
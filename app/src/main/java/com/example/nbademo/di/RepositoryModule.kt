package com.example.nbademo.di

import com.example.nbademo.data.domain.repository.NbaRepository
import com.example.nbademo.data.domain.repository.NbaRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Modul pro mapování rozhraní repozitářů na jejich konkrétní implementace.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNbaRepository(
        nbaRepositoryImpl: NbaRepositoryImpl
    ): NbaRepository
}
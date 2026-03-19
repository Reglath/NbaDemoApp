package com.example.nbademo

import com.example.nbademo.data.remote.dto.PlayerDto
import com.example.nbademo.data.remote.dto.TeamDto
import com.example.nbademo.data.remote.dto.toDomain
import org.junit.Assert.assertEquals
import org.junit.Test

class DtoTests {

    @Test
    fun toDomainShouldConnectNameAndSurname() {
        val dto = PlayerDto(
            id = 1,
            firstName = "LeBron",
            lastName = "James",
            position = "F",
            height = "6-9",
            weight = "250",
            jerseyNumber = "23",
            college = "High School",
            country = "USA",
            draftYear = 2003,
            draftRound = 1,
            draftNumber = 1,
            team = null
        )

        val domain = dto.toDomain()

        assertEquals("LeBron James", domain.name)
    }

    @Test
    fun toDomainShouldReturnUndraftedIfYearIsNull() {
        val dto = PlayerDto(
            id = 2,
            firstName = "Austin",
            lastName = "Reaves",
            position = "G",
            height = null, weight = null, jerseyNumber = null, college = null, country = null,
            draftYear = null, draftRound = null, draftNumber = null,
            team = null
        )

        val domain = dto.toDomain()

        assertEquals("Nedraftován", domain.draftInfo)
    }

    @Test
    fun toDomainShouldMapCorrectly() {
        val dto = TeamDto(
            id = 14,
            fullName = "Los Angeles Lakers",
            name = "Lakers",
            abbreviation = "LAL",
            city = "Los Angeles",
            conference = "West",
            division = "Pacific"
        )

        val domain = dto.toDomain()

        assertEquals(14, domain.id)
        assertEquals("Los Angeles Lakers", domain.fullName)
        assertEquals("LAL", domain.abbreviation)
    }

    @Test
    fun toDomainShouldUseDefaultValuesIfNull() {
        val dto = TeamDto(
            id = 99,
            fullName = null,
            name = null,
            abbreviation = null,
            city = null,
            conference = null,
            division = null
        )

        val domain = dto.toDomain()

        assertEquals("Neznámý tým", domain.fullName)
        assertEquals("???", domain.abbreviation)
        assertEquals("N/A", domain.conference)
    }

}
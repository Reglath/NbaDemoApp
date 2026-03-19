package com.example.nbademo.data.remote.dto

import com.example.nbademo.data.domain.model.Team
import com.google.gson.annotations.SerializedName

/**
 * Datový transfer objekt (DTO) reprezentující team tak, jak přichází z API JSONu.
 */
data class TeamDto(
    @SerializedName("id") val id: Int,
    @SerializedName("conference") val conference: String?,
    @SerializedName("division") val division: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("abbreviation") val abbreviation: String?
)

/**
 * Rozšiřující funkce, která transformuje [TeamDto] na doménový model [Team].
 * Provádí čištění dat (null-safety) a formátování (např. složení informací o draftu).
 * * @return Inicializovaný objekt [Team] připravený pro UI vrstvu.
 */
fun TeamDto.toDomain(): Team {
    return Team(
        id = id,
        fullName = fullName ?: "Neznámý tým",
        name = name ?: "Neznámý tým",
        abbreviation = abbreviation ?: "???",
        city = city ?: "Neznámé město",
        conference = conference ?: "N/A",
        division = division ?: "N/A",
        imageUrl = "https://ui-avatars.com/api/?name=$abbreviation&background=random&size=400&length=3"
    )
}
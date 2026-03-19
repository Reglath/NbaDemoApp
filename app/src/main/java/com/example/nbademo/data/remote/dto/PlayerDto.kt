package com.example.nbademo.data.remote.dto

import com.example.nbademo.data.domain.model.Player
import com.google.gson.annotations.SerializedName

/**
 * Datový transfer objekt (DTO) reprezentující hráče tak, jak přichází z API JSONu.
 */
data class PlayerDto(
    @SerializedName("id") val id: Int,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("position") val position: String?,
    @SerializedName("height") val height: String?,
    @SerializedName("weight") val weight: String?,
    @SerializedName("jersey_number") val jerseyNumber: String?,
    @SerializedName("college") val college: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("draft_year") val draftYear: Int?,
    @SerializedName("draft_round") val draftRound: Int?,
    @SerializedName("draft_number") val draftNumber: Int?,
    @SerializedName("team") val team: TeamDto?
)

/**
 * Rozšiřující funkce, která transformuje [PlayerDto] na doménový model [Player].
 * Provádí čištění dat (null-safety) a formátování (např. složení informací o draftu).
 * * @return Inicializovaný objekt [Player] připravený pro UI vrstvu.
 */
fun PlayerDto.toDomain(): Player {
    val draft = if (draftYear != null) {
        "$draftYear (Kolo: ${draftRound ?: "N/A"}, Číslo: ${draftNumber ?: "N/A"})"
    } else "Nedraftován"

    return Player(
        id = id,
        name = "${firstName ?: ""} ${lastName ?: ""}".trim(),
        position = position ?: "N/A",
        height = height ?: "N/A",
        weight = weight ?: "N/A",
        jerseyNumber = jerseyNumber ?: "N/A",
        college = college ?: "N/A",
        country = country ?: "N/A",
        draftInfo = draft,
        teamName = team?.fullName ?: "Neznámý tým",
        teamId = team?.id ?: -1,
        imageUrl = "https://ui-avatars.com/api/?name=$firstName+$lastName&background=random&size=400",
    )
}
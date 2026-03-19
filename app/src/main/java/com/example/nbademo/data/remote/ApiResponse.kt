package com.example.nbademo.data.remote

import com.example.nbademo.data.remote.dto.MetaDto
import com.example.nbademo.data.remote.dto.PlayerDto
import com.example.nbademo.data.remote.dto.TeamDto
import com.google.gson.annotations.SerializedName

/**
 * Response classy pro převod api dat do domain modelů.
 */
data class PlayerListResponse(
    @SerializedName("data") val data: List<PlayerDto>,
    @SerializedName("meta") val meta: MetaDto
)

data class PlayerDetailResponse(
    @SerializedName("data") val data: PlayerDto
)

data class TeamDetailResponse(
    @SerializedName("data") val data: TeamDto
)
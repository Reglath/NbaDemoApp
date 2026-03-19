package com.example.nbademo.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Datový transfer objekt (DTO) reprezentující metadata tak, jak přichází z API JSONu.
 */
data class MetaDto(
    @SerializedName("next_cursor") val nextCursor: Int?,
    @SerializedName("per_page") val perPage: Int
)
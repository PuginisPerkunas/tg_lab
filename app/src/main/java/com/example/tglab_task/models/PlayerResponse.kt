package com.example.tglab_task.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlayerResponse(
    @Json(name = "data")
    val playerData: List<PlayerData>,
    @Json(name = "meta")
    val meta: Meta
)
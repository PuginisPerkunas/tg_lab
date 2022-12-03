package com.example.tglab_task.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeamGamesResponse(
    @Json(name = "data")
    val teamGames: List<TeamGame>,
    @Json(name = "meta")
    val meta: Meta
)
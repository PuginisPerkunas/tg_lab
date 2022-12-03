package com.example.tglab_task.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeamsResponse(
    @Json(name = "data")
    val teams: List<Data>,
    @Json(name = "meta")
    val meta: Meta
)
package com.example.tglab_task.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeamGame(
    @Json(name = "date")
    val date: String,
    @Json(name = "home_team")
    val home_team: Team,
    @Json(name = "home_team_score")
    val home_team_score: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "period")
    val period: Int,
    @Json(name = "postseason")
    val postseason: Boolean,
    @Json(name = "season")
    val season: Int,
    @Json(name = "status")
    val status: String,
    @Json(name = "time")
    val time: String,
    @Json(name = "visitor_team")
    val visitor_team: Team,
    @Json(name = "visitor_team_score")
    val visitor_team_score: Int
)
package com.example.tglab_task.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class PlayerData(
    @Json(name = "first_name")
    val first_name: String,
    @Json(name = "height_feet")
    val height_feet: Float?,
    @Json(name = "height_inches")
    val height_inches: Float?,
    @Json(name = "id")
    val id: Int,
    @Json(name = "last_name")
    val last_name: String,
    @Json(name = "position")
    val position: String,
    @Json(name = "team")
    val team: Team,
    @Json(name = "weight_pounds")
    val weight_pounds: Float?
): Parcelable
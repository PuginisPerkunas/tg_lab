package com.example.tglab_task.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meta(
    @Json(name = "current_page")
    val current_page: Int,
    @Json(name = "next_page")
    val next_page: Int?,
    @Json(name = "per_page")
    val per_page: Int,
    @Json(name = "total_count")
    val total_count: Int,
    @Json(name = "total_pages")
    val total_pages: Int
)
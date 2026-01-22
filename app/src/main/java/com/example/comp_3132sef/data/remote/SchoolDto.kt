package com.example.comp_3132sef.data.remote

import com.squareup.moshi.Json

data class SchoolDto(
    @Json(name = "ENGLISH NAME")
    val englishName: String?,

    @Json(name = "中文名稱")
    val chineseName: String?,

    @Json(name = "LATITUDE")
    val latitude: Double?,

    @Json(name = "LONGITUDE")
    val longitude: Double?,

    @Json(name = "WEBSITE")
    val website: String?
)
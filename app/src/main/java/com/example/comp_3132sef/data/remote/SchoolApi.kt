package com.example.comp_3132sef.data.remote

import retrofit2.http.GET

interface SchoolApi {

    @GET("SCH_LOC_EDB.json")
    suspend fun getSchools(): List<SchoolDto>
}
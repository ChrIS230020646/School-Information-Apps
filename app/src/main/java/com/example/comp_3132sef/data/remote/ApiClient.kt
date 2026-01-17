package com.example.comp_3132sef.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(
            "https://www.edb.gov.hk/attachment/en/student-parents/sch-info/sch-search/sch-location-info/"
        )
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val schoolApi: SchoolApi =
        retrofit.create(SchoolApi::class.java)
}

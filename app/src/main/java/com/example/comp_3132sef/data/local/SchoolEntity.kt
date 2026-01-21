package com.example.comp_3132sef.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schools")
data class SchoolEntity(
    @PrimaryKey val id: String,
    val englishName: String,
    val chineseName: String?,
    val latitude: Double,
    val longitude: Double
)


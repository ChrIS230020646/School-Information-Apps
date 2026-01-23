package com.example.comp_3132sef.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schools")
data class SchoolEntity(
    @PrimaryKey val id: String,
    val englishName: String,
    val chineseName: String?,
    val englishCategory: String?,
    val chineseCategory: String?,
    val englishAddress: String?,
    val chineseAddress: String?,
    val telephone: String?,
    val district: String?,
    val chineseDistrict: String?,
    val website: String?,
    val religion: String?,
    val chineseReligion: String?,
    val studentsGender: String?,
    val chineseStudentsGender: String?,
    val session: String?,
    val latitude: Double?,
    val longitude: Double?
)
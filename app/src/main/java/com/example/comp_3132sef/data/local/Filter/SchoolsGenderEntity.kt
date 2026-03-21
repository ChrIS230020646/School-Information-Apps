package com.example.comp_3132sef.data.local.Filter

import androidx.room.Entity

@Entity(tableName = "schools")
data class SchoolsGenderEntity(
    val studentsGender: String ,
    val chineseStudentsGender: String
)

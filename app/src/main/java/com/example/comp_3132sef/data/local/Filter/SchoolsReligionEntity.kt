package com.example.comp_3132sef.data.local.Filter

import androidx.room.Entity

@Entity(tableName = "schools")
data class SchoolsReligionEntity (
    val religion: String ,
    val chineseReligion : String ,
)

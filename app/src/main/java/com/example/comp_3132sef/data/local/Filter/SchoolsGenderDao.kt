package com.example.comp_3132sef.data.local.Filter

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface SchoolsGenderDao {
    @Query("""
        SELECT distinct studentsGender ,chineseStudentsGender FROM schools
    """)
    fun getSchoolsGender(): Flow<List<SchoolsGenderEntity>>
}
package com.example.comp_3132sef.data.local.Filter

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolsAddressDao {
    @Query("""
        SELECT distinct englishAddress,chineseAddress FROM schools
    """)
    fun getSchoolsAddress(): Flow<List<SchoolsAddressEntity>>
}
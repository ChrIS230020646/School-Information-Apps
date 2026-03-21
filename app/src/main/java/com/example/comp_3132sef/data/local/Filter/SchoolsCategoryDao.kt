package com.example.comp_3132sef.data.local.Filter

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolsCategoryDao {
    @Query("""
        SELECT distinct englishCategory,chineseCategory FROM schools
        where
not( (UPPER(chineseCategory) in("N.A." , "","NOT APPLICABLE","不適用")
or chineseCategory is null)
and          
(UPPER(englishCategory) in("N.A." , "","NOT APPLICABLE")  or englishCategory is null)
)
        
    """)
    fun getSchoolsCategory(): Flow<List<SchoolsCategoryEntity>>
}
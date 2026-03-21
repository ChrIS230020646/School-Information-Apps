package com.example.comp_3132sef.data.local.Filter

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface SchoolsDistrictDao {
    @Query("""
        SELECT distinct district ,chineseDistrict FROM schools
        where 
        not( (UPPER(chineseDistrict)  in("N.A." , "","NOT APPLICABLE","不適用")
or chineseDistrict is null)
and          
(UPPER(district) in("N.A." , "","NOT APPLICABLE")  or district is null)
)
        
    
    """)
    fun getSchoolsDistrict(): Flow<List<SchoolsDistrictEntity>>

}
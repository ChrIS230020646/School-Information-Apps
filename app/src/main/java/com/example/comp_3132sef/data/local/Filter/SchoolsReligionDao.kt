package com.example.comp_3132sef.data.local.Filter

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface SchoolsReligionDao {
    @Query("""
        SELECT distinct religion ,chineseReligion FROM schools
        where 
        not( (UPPER(chineseReligion)  in("N.A." , "","NOT APPLICABLE","不適用")
or chineseReligion is null)
and          
(UPPER(religion) in("N.A." , "","NOT APPLICABLE")  or religion is null)
)
    """)
    fun getSchoolsReligion(): Flow<List<SchoolsReligionEntity>>

}
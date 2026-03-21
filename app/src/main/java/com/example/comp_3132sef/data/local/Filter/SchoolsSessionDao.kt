package com.example.comp_3132sef.data.local.Filter

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface SchoolsSessionDao {

    @Query("""
        SELECT distinct session ,chineseSession FROM schools
        where 
        not( (UPPER(chineseSession)  in("N.A." , "","NOT APPLICABLE","不適用")
or chineseSession is null)
and          
(UPPER(session) in("N.A." , "","NOT APPLICABLE")  or session is null)
)
    """)
    fun getSchoolsSession(): Flow<List<SchoolsSessionEntity>>
}
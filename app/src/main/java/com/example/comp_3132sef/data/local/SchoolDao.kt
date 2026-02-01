package com.example.comp_3132sef.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {

    @Query("SELECT * FROM schools")
    fun observeSchools(): Flow<List<SchoolEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(schools: List<SchoolEntity>)

    @Query("DELETE FROM schools")
    suspend fun clearAll()

    @Query("""
        SELECT * FROM schools 
        WHERE chineseName LIKE '%' || :query || '%' 
        OR englishName LIKE '%' || :query || '%' 
        OR chineseAddress LIKE '%' || :query || '%' 
        OR telephone LIKE '%' || :query || '%'
        OR chineseDistrict LIKE '%' || :query || '%'
    """)
    fun searchSchools(query: String): Flow<List<SchoolEntity>>


    @Query("SELECT * FROM schools WHERE ID IN (:schoolIds)")
    fun getSchoolsByIds(schoolIds: Set<String>): Flow<List<SchoolEntity>>

    @Query("""
        SELECT * FROM schools 
        WHERE ID = :ID 
    """)
    fun getSchoolsByID(ID: String): Flow<List<SchoolEntity>>

//    @Query("""
//    SELECT schools.*
//    FROM schools
//    INNER JOIN favorites ON schools.englishName = favorites.englishName
//""")
//    fun getAllFavoriteSchoolsInfo(): Flow<List<SchoolEntity>>
}


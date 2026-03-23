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

    @Query("""
    SELECT * FROM schools 
    WHERE (chineseName LIKE '%' || :query || '%' 
        OR englishName LIKE '%' || :query || '%' 
        OR chineseAddress LIKE '%' || :query || '%' 
        OR telephone LIKE '%' || :query || '%'
        OR chineseDistrict LIKE '%' || :query || '%')
    -- 使用簡化的方式：Room 會自動處理 List 的展開
    AND (:sessionsCount = 0 OR session IN (:sessions) OR chineseSession IN (:sessions))
    AND (:gendersCount = 0 OR studentsGender IN (:genders) OR chineseStudentsGender IN (:genders))
    AND (:districtsCount = 0 OR district IN (:districts) OR chineseDistrict IN (:districts))
    AND (:religionsCount = 0 OR religion IN (:religions) OR chineseReligion IN (:religions))
    AND (:categoriesCount = 0 OR englishCategory IN (:categories) OR chineseCategory IN (:categories))
""")
    fun searchSchoolsWithFilters(
        query: String?,
        sessions: List<String>?,
        sessionsCount: Int, // 傳入 list.size
        genders: List<String>?,
        gendersCount: Int,
        districts: List<String>?,
        districtsCount: Int,
        religions: List<String>?,
        religionsCount: Int,
        categories: List<String>?,
        categoriesCount: Int
    ): Flow<List<SchoolEntity>>

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


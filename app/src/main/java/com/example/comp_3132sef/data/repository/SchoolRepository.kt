package com.example.comp_3132sef.data.repository

import android.content.Context
import com.example.comp_3132sef.data.local.DatabaseProvider
import com.example.comp_3132sef.data.local.FavoriteEntity
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.data.remote.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SchoolRepository(context: Context) {

    private val db = DatabaseProvider.getDatabase(context)

    private val schoolDao = db.schoolDao()
    private val favoriteDao = db.favoriteDao()

    fun observeSchools(): Flow<List<String>> =
        schoolDao.observeSchools()
            .map { list -> list.map { it.englishName } }

    suspend fun refreshSchools() {
        val remote = ApiClient.schoolApi.getSchools()

        val entities = remote
            .filter { it.englishName != null && it.latitude != null && it.longitude != null }
            .map {
                SchoolEntity(
                    id = it.englishName!!,
                    englishName = it.englishName!!,
                    chineseName = it.chineseName,
                    latitude = it.latitude!!,
                    longitude = it.longitude!!,
                    englishCategory =it.englishCategory!!,
                    chineseCategory =it.chineseCategory!!,
                    englishAddress =it.englishAddress,
                    chineseAddress =it.chineseAddress,
                    telephone =it.session,
                    district =it.district,
                    chineseDistrict =it.chineseDistrict,
                    website =it.website,
                    religion =it.religion,
                    chineseReligion =it.chineseReligion,
                    studentsGender =it.studentsGender,
                    chineseStudentsGender=it.chineseStudentsGender,
                    chineseSession = it.chineseSession,
                    session =it.session
                )
            }

        schoolDao.clearAll()
        schoolDao.insertAll(entities)
    }

    fun observeSchoolLocations(): Flow<List<SchoolEntity>> =
        schoolDao.observeSchools()

    fun observeSchoolEntities(): Flow<List<SchoolEntity>> =
        schoolDao.observeSchools()

    fun observeFavorites(): Flow<Set<String>> =
        favoriteDao.observeFavorites()
            .map { list -> list.map { it.englishName }.toSet() }

    suspend fun toggleFavorite(name: String) {
        if (favoriteDao.isFavorite(name)) {
            favoriteDao.remove(FavoriteEntity(name))
        } else {
            favoriteDao.add(FavoriteEntity(name))
        }
    }
}
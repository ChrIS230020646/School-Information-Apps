package com.example.comp_3132sef.data.repository

import android.content.Context
import com.example.comp_3132sef.data.local.DatabaseProvider
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.data.remote.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SchoolRepository(context: Context) {

    private val dao = DatabaseProvider
        .getDatabase(context)
        .schoolDao()

    fun observeSchools(): Flow<List<String>> {
        return dao.observeSchools()
            .map { list -> list.map { it.englishName } }
    }

    suspend fun refreshSchools() {
        val remote = ApiClient.schoolApi.getSchools()
        val entities = remote.mapNotNull { it.englishName }
            .map { SchoolEntity(it) }

        dao.clearAll()
        dao.insertAll(entities)
    }
}
package com.example.comp_3132sef.data.repository

import android.content.Context
import com.example.comp_3132sef.data.local.DatabaseProvider
import com.example.comp_3132sef.data.local.Filter.SchoolsCategoryDao
import com.example.comp_3132sef.data.local.Filter.SchoolsCategoryEntity
import com.example.comp_3132sef.data.local.Filter.SchoolsDistrictEntity
import com.example.comp_3132sef.data.local.Filter.SchoolsGenderEntity
import com.example.comp_3132sef.data.local.Filter.SchoolsReligionEntity
import com.example.comp_3132sef.data.local.Filter.SchoolsSessionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SchoolsFilterCategoryRepository(context: Context) {
    private val db = DatabaseProvider.getDatabase(context)


    private val schoolsCategoryDao = db.schoolsCategoryDao()
    private val schoolsDistrictDao = db.schoolsDistrictDao()
    private val schoolsReligionDao = db.schoolsReligionDao()
    private val schoolsGenderDao = db.schoolsGenderDao()
    private val schoolsSessionDao=db.schoolsSessionDao()
    fun observeSchoolsCategory(): Flow<List<SchoolsCategoryEntity>> =
        schoolsCategoryDao.getSchoolsCategory()
    fun observeSchoolsDistrict(): Flow<List<SchoolsDistrictEntity>> =
        schoolsDistrictDao.getSchoolsDistrict()
    fun observeSchoolsReligion(): Flow<List<SchoolsReligionEntity>> =
        schoolsReligionDao.getSchoolsReligion()
    fun observeSchoolsGender(): Flow<List<SchoolsGenderEntity>> =
        schoolsGenderDao.getSchoolsGender()
    fun observeSchoolsSession(): Flow<List<SchoolsSessionEntity>> =
        schoolsSessionDao.getSchoolsSession()


}
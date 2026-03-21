package com.example.comp_3132sef.ui.school

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.comp_3132sef.data.local.DatabaseProvider
import com.example.comp_3132sef.data.local.Filter.SchoolsCategoryEntity
import com.example.comp_3132sef.data.local.Filter.SchoolsDistrictEntity
import com.example.comp_3132sef.data.local.Filter.SchoolsGenderEntity
import com.example.comp_3132sef.data.local.Filter.SchoolsReligionEntity
import com.example.comp_3132sef.data.local.Filter.SchoolsSessionEntity
import com.example.comp_3132sef.data.local.SchoolEntity
import kotlinx.coroutines.flow.*
import com.example.comp_3132sef.data.repository.SchoolRepository
import com.example.comp_3132sef.data.repository.SchoolsFilterCategoryRepository

class FilterViewModel(application: Application) : AndroidViewModel(application) {
    // query used for filtering (backing MutableStateFlow)
    private val _filterQuery = MutableStateFlow("")
    val filterQuery: StateFlow<String> = _filterQuery.asStateFlow()

    private val repository = SchoolsFilterCategoryRepository(application)
    private val db = DatabaseProvider.getDatabase(application)
    private val schoolDao = db.schoolDao()

//    val schoolSessionEntities: StateFlow<List<SchoolsSessionEntity>> =
//        repository.observeSchoolsCategory()
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5_000),
//                initialValue = emptyList()
//            )

    val getSchoolsCategoryEntity: StateFlow<List<SchoolsCategoryEntity>> =
        repository.observeSchoolsCategory()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val getSchoolsDistrictEntity: StateFlow<List<SchoolsDistrictEntity>> =
        repository.observeSchoolsDistrict()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val getSchoolsReligionEntity: StateFlow<List<SchoolsReligionEntity>> =
        repository.observeSchoolsReligion()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val getSchoolsGenderEntity: StateFlow<List<SchoolsGenderEntity>> =
        repository.observeSchoolsGender()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val getSchoolsSessionEntities: StateFlow<List<SchoolsSessionEntity>> =
       repository.observeSchoolsSession()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )


    fun setFilterQuery(query: String) {
        _filterQuery.value = query
    }






}


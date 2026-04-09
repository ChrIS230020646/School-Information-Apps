package com.example.comp_3132sef.ui.school

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.comp_3132sef.data.local.SchoolDao


import androidx.lifecycle.viewModelScope
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.comp_3132sef.data.local.DatabaseProvider
import kotlinx.coroutines.flow.*
import com.example.comp_3132sef.data.local.SchoolEntity
import kotlin.String


class SearchSchoolViewModel(application: Application) : AndroidViewModel(application) {
    private val _searchQuery = MutableStateFlow("")
    private val _searchFilter = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val searchFilter = _searchFilter.asStateFlow()
    val searchQuery = _searchQuery.asStateFlow()
//    private val repository = SchoolRepository(application)
    private val db = DatabaseProvider.getDatabase(application)
    private val schoolDao = db.schoolDao()
//@OptIn(ExperimentalCoroutinesApi::class)
    val searchResults = combine(_searchQuery, _searchFilter) { query, filters ->

        Pair(query, filters)
    }.flatMapLatest { (query, filters) ->
    Log.d("Debug", "schoolDao")

        schoolDao.searchSchoolsWithFilters(
            query = query,
            sessions = filters["sessions"].takeIf { !it.isNullOrEmpty() },
            genders = filters["genders"].takeIf { !it.isNullOrEmpty() },
            districts = filters["districts"].takeIf { !it.isNullOrEmpty() },
            religions = filters["religion"].takeIf { !it.isNullOrEmpty() },
            categories = filters["category"].takeIf { !it.isNullOrEmpty() } ,
            sessionsCount = (filters["sessions"])?.size ?: 0,

            gendersCount = filters["genders"]?.size ?: 0,

            districtsCount = filters["districts"]?.size ?: 0,

            religionsCount = filters["religion"]?.size ?: 0,

            categoriesCount = filters["category"]?.size ?: 0
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }
    fun clearQuery() {
        _searchQuery.value = ""
    }
    fun onUpdateFilter(filter: Map<String, List<String>>) {
        _searchFilter.value = filter
    }
    fun clearAllFilters() {
        _searchFilter.value = emptyMap()
        Log.d("SearchVM", "Filters have been cleared.")
    }
    private val _selectedSchool = MutableStateFlow<SchoolEntity?>(null)

    val selectedSchool: StateFlow<SchoolEntity?> = _selectedSchool.asStateFlow()


    fun selectSchool(school: SchoolEntity) {
        _selectedSchool.value = school
    }


    fun clearSelection() {
        _selectedSchool.value = null
    }

}
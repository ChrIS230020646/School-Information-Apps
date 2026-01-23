package com.example.comp_3132sef.ui.school

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.comp_3132sef.data.local.SchoolDao


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.comp_3132sef.data.local.DatabaseProvider
import kotlinx.coroutines.flow.*
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.data.repository.SchoolRepository

@Database(entities = [SchoolEntity::class], version = 1)
abstract class SchoolDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao
}

class SearchSchoolViewModel(application: Application) : AndroidViewModel(application) {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val repository = SchoolRepository(application)
    private val db = DatabaseProvider.getDatabase(application)

    private val schoolDao = db.schoolDao()
    val searchResults = _searchQuery
        .flatMapLatest { query -> schoolDao.searchSchools(query) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }


}
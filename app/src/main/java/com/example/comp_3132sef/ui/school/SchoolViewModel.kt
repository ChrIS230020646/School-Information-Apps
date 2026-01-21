package com.example.comp_3132sef.ui.school

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.comp_3132sef.data.repository.SchoolRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.comp_3132sef.data.local.SchoolEntity
import kotlin.math.asin
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.sqrt
import kotlin.math.pow
import kotlinx.coroutines.flow.MutableStateFlow

class SchoolViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _selectedSchool = MutableStateFlow<SchoolEntity?>(null)
    val selectedSchool: StateFlow<SchoolEntity?> = _selectedSchool

    fun openSchoolMap(school: SchoolEntity) {
        _selectedSchool.value = school
    }

    fun closeSchoolMap() {
        _selectedSchool.value = null
    }

    private val repository = SchoolRepository(application)

    val schools: StateFlow<List<String>> =
        repository.observeSchools()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val schoolEntities: StateFlow<List<SchoolEntity>> =
        repository.observeSchoolEntities()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun Context.isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    init {
        viewModelScope.launch {
            if (getApplication<Application>().isOnline()) {
                try {
                    repository.refreshSchools()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    val favorites: StateFlow<Set<String>> =
        repository.observeFavorites()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySet()
            )

    fun toggleFavorite(name: String) {
        viewModelScope.launch {
            repository.toggleFavorite(name)
        }
    }

    fun distanceInKm(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {

        val r = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a =
            sin(dLat / 2).pow(2.0) +
                    cos(Math.toRadians(lat1)) *
                    cos(Math.toRadians(lat2)) *
                    sin(dLon / 2).pow(2.0)
        return 2 * r * asin(sqrt(a))

    }

    val schoolsFromDb: StateFlow<List<SchoolEntity>> =
        repository.observeSchoolEntities()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}


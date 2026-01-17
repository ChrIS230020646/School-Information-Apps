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

class SchoolViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = SchoolRepository(application)

    val schools: StateFlow<List<String>> =
        repository.observeSchools()
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
}


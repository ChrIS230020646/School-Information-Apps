package com.example.comp_3132sef.data.local

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.util.Locale

object SchoolDataHolder {
    var selectedSchool: SchoolEntity? = null
    var isZh by mutableStateOf(Locale.getDefault().language == "zh")
    var query =""
    var currencySelectedFilters by mutableStateOf<Map<String, List<String>>>(emptyMap())
    var refreshSchoolsCount =0
    var isRefresh =false

}
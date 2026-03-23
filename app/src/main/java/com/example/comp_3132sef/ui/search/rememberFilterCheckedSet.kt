package com.example.comp_3132sef.ui.search

import FilterCheckedSet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.comp_3132sef.ui.school.FilterViewModel

@Composable
fun rememberFilterCheckedSet(viewModel: FilterViewModel, isZh: Boolean): FilterCheckedSet {
    val schoolsSession by viewModel.getSchoolsSessionEntities.collectAsState(initial = emptyList())
    val schoolsGender by viewModel.getSchoolsGenderEntity.collectAsState(initial = emptyList())
    val schoolsReligion by viewModel.getSchoolsReligionEntity.collectAsState(initial = emptyList())
    val schoolsCategory by viewModel.getSchoolsCategoryEntity.collectAsState(initial = emptyList())
    val schoolsDistrict by viewModel.getSchoolsDistrictEntity.collectAsState(initial = emptyList())

    return remember(schoolsSession, schoolsGender, schoolsReligion, schoolsCategory, schoolsDistrict, isZh) {
        FilterCheckedSet(
            sessionList = schoolsSession.map { if (isZh) it.chineseSession else it.session },
            genderList = schoolsGender.map { if (isZh) it.chineseStudentsGender else it.studentsGender },
            religionList = schoolsReligion.map { if (isZh) it.chineseReligion else it.religion },
            categoryList = schoolsCategory.map { if (isZh) (it.chineseCategory ?: "") else (it.englishCategory ?: "") },
            districtList = schoolsDistrict.map { if (isZh) it.chineseDistrict else it.district },

            sessionChecked = List(schoolsSession.size) { false },
            genderChecked = List(schoolsGender.size) { false },
            religionChecked = List(schoolsReligion.size) { false },
            categoryChecked = List(schoolsCategory.size) { false },
            districtChecked = List(schoolsDistrict.size) { false },
            onUpdate = { _, _, _ -> } // 這裡可以擴展自定義邏輯
        )
    }
}
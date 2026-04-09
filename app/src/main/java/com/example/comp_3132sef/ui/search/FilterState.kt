package com.example.comp_3132sef.ui.search

import com.example.comp_3132sef.data.local.Filter.FilterGroup

class FilterState(
    val session: FilterGroup,
    val gender: FilterGroup,
    val religion: FilterGroup,
    val category: FilterGroup,
    val district: FilterGroup,
    private val onUpdate: (FilterType, List<Boolean>) -> Unit
) {
    enum class FilterType { SESSION, GENDER, RELIGION, CATEGORY, DISTRICT }

    // Setter:
    fun updateChecked(type: FilterType, index: Int, isChecked: Boolean) {
        val currentGroup = when (type) {
            FilterType.SESSION -> session
            FilterType.GENDER -> gender
            FilterType.RELIGION -> religion
            FilterType.CATEGORY -> category
            FilterType.DISTRICT -> district
        }

        val newList = currentGroup.checkedStates.toMutableList()
        if (index in newList.indices) {
            newList[index] = isChecked
            onUpdate(type, newList)
        }
    }
}
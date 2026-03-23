package com.example.comp_3132sef.data.local.Filter

data class FilterGroup(
    val items: List<String> = emptyList(),
    val checkedStates: List<Boolean> = emptyList()
) {

    fun getSelectedItems(): List<String> {
        return items.filterIndexed { index, _ -> checkedStates.getOrElse(index) { false } }
    }
}

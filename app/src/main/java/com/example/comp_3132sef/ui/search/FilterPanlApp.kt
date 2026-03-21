package com.example.comp_3132sef.ui.search
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.comp_3132sef.data.local.SchoolDao
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.ui.school.FilterViewModel

// 1. Data model for the filter state
@Composable
fun FilterPanelApp(
    viewModel: FilterViewModel,
    isZh: Boolean,
    onBack: () -> Unit,
    onConfirm: (Map<String, List<String>>) -> Unit
) {
    // 1. Data Collection (Stay the same)
    val schoolsSession by viewModel.getSchoolsSessionEntities.collectAsState()
    val schoolsGender by viewModel.getSchoolsGenderEntity.collectAsState()
    val schoolsReligion by viewModel.getSchoolsReligionEntity.collectAsState()
    val schoolsCategory by viewModel.getSchoolsCategoryEntity.collectAsState()
    val schoolsDistrict by viewModel.getSchoolsDistrictEntity.collectAsState()

    // 2. Transformed Lists (Cached with remember)
    val sessionList = remember(schoolsSession, isZh) { schoolsSession.map { if (isZh) it.chineseSession else it.session } }
    val genderList = remember(schoolsGender, isZh) { schoolsGender.map { if (isZh) it.chineseStudentsGender else it.studentsGender } }
    val religionList = remember(schoolsReligion, isZh) { schoolsReligion.map { if (isZh) it.chineseReligion else it.religion } }
    val categoryList = remember(schoolsCategory, isZh) { schoolsCategory.map { if (isZh) (it.chineseCategory ?: "") else (it.englishCategory ?: "") } }
    val districtList = remember(schoolsDistrict, isZh) { schoolsDistrict.map { if (isZh) it.chineseDistrict else it.district } }

    // 3. States for Checkboxes
    var sessionChecked by remember(schoolsSession) { mutableStateOf(List(schoolsSession.size) { false }) }
    var genderChecked by remember(schoolsGender) { mutableStateOf(List(schoolsGender.size) { false }) }
    var religionChecked by remember(schoolsReligion) { mutableStateOf(List(schoolsReligion.size) { false }) }
    var categoryChecked by remember(schoolsCategory) { mutableStateOf(List(schoolsCategory.size) { false }) }
    var districtChecked by remember(schoolsDistrict) { mutableStateOf(List(schoolsDistrict.size) { false }) }

    // --- UI STRUCTURE ---
    Scaffold(
        bottomBar = {
            // FIXED BOTTOM BUTTONS
            Surface(shadowElevation = 8.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // BACK BUTTON
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onBack
                    ) {
                        Text(if (isZh) "返回" else "Back")
                    }

                    // CONFIRM BUTTON
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            // Collect all selected items
                            val selectedData = mutableMapOf<String, List<String>>()
                            selectedData["sessions"] = sessionList.filterIndexed { i, _ -> sessionChecked[i] }
                            selectedData["genders"] = genderList.filterIndexed { i, _ -> genderChecked[i] }
                            selectedData["districts"] = districtList.filterIndexed { i, _ -> districtChecked[i] }

                            onConfirm(selectedData)
                        }
                    ) {
                        Text(if (isZh) "確認" else "Confirm")
                    }
                }
            }
        }
    ) { innerPadding ->
        // SCROLLABLE CONTENT
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Sessions
            item { SectionHeader(if(isZh) "授課時間" else "Session") }
            itemsIndexed(sessionList) { index, label ->
                FilterRow(label, sessionChecked[index]) { isChecked ->
                    sessionChecked = sessionChecked.toMutableList().apply { this[index] = isChecked }
             n   }
            }

            // Genders
            item { SectionHeader(if(isZh) "學生性別" else "Gender") }
            itemsIndexed(genderList) { index, label ->
                FilterRow(label, genderChecked[index]) { isChecked ->
                    genderChecked = genderChecked.toMutableList().apply { this[index] = isChecked }
                }
            }

            // Districts
            item { SectionHeader(if(isZh) "地區" else "District") }
            itemsIndexed(districtList) { index, label ->
                FilterRow(label, districtChecked[index]) { isChecked ->
                    districtChecked = districtChecked.toMutableList().apply { this[index] = isChecked }
                }
            }

        //Religion
            item { SectionHeader(if(isZh) "宗教" else "Religion") }
            itemsIndexed(religionList) { index, label ->
                FilterRow(label, religionChecked[index]) { isChecked ->
                    religionChecked = religionChecked.toMutableList().apply { this[index] = isChecked }
                }
            }
            //Category
            item { SectionHeader(if(isZh) "類別" else "category") }
            itemsIndexed(categoryList) { index, label ->
                FilterRow(label, categoryChecked[index]) { isChecked ->
                    categoryChecked = categoryChecked.toMutableList().apply { this[index] = isChecked }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        color = MaterialTheme.colorScheme.primary
    )
    HorizontalDivider(thickness = 1.dp)
}

@Composable
fun FilterRow(label: String, isChecked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.weight(1f))
        Checkbox(checked = isChecked, onCheckedChange = onToggle)
    }
}
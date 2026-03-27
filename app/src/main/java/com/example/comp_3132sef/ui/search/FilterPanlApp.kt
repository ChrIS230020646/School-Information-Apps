package com.example.comp_3132sef.ui.search
import FilterCheckedSet
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.comp_3132sef.R
import com.example.comp_3132sef.data.local.SchoolDao
import com.example.comp_3132sef.data.local.SchoolDataHolder
import com.example.comp_3132sef.data.local.SchoolDataHolder.isZh
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.ui.school.FilterViewModel

// 1. Data model for the filter state
@Composable
fun FilterPanelApp(
    filterCheckedSet:FilterCheckedSet,
    selectSet:Map<String, List<String>>,
    clarAll:() ->Unit,
    onBack: () -> Unit,
    onConfirm: (Map<String, List<String>>) -> Unit
) {

    val sessionList =filterCheckedSet.sessionList
    val genderList = filterCheckedSet.genderList
    val religionList= filterCheckedSet.religionList
    val categoryList= filterCheckedSet.categoryList
    val districtList=filterCheckedSet.districtList

//    val selectSet=SchoolDataHolder.currencySelectedFilters
    // 3. States for Checkboxes

// Session
    var sessionChecked by remember(sessionList, isZh, selectSet) {
        val saved = selectSet["sessions"].orEmpty()
        mutableStateOf(List(sessionList.size) { i -> saved.contains(sessionList[i]) })
    }

// Gender
    var genderChecked by remember(genderList, isZh, selectSet) {
        val saved = selectSet["genders"].orEmpty()
        mutableStateOf(List(genderList.size) { i -> saved.contains(genderList[i]) })
    }

// Religion
    var religionChecked by remember(religionList, isZh, selectSet) {
        val saved = selectSet["religion"].orEmpty()
        mutableStateOf(List(religionList.size) { i -> saved.contains(religionList[i]) })
    }

// District
    var districtChecked by remember(districtList, isZh, selectSet) {
        val saved = selectSet["districts"].orEmpty()
        mutableStateOf(List(districtList.size) { i -> saved.contains(districtList[i]) })
    }

// Category
    var categoryChecked by remember(categoryList, isZh, selectSet) {
        val saved = selectSet["category"].orEmpty()
        mutableStateOf(List(categoryList.size) { i -> saved.contains(categoryList[i]) })
    }

    var isSessionExpanded by rememberSaveable { mutableStateOf(true) }
    var isGenderExpanded by rememberSaveable { mutableStateOf(true) }
    var isReligionExpanded by rememberSaveable { mutableStateOf(true) }
    var isDistrictExpanded by rememberSaveable { mutableStateOf(true) }
    var isCategoryExpanded by rememberSaveable { mutableStateOf(true) }

    //  UI STRUCTURE
    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 4.dp, // Adds a subtle shadow to separate from content
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(id = R.string.Filters)
//                    text = if (isZh) "篩選器" else "Filters"
                    ,
                    style = MaterialTheme.typography.headlineSmall, // Or titleLarge
                    modifier = Modifier.padding(16.dp)
                )
            }
        },
        bottomBar = {
            // Bottom button
            Surface(shadowElevation = 8.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    //  Clear Button
                    TextButton(
                        onClick = {

                            sessionChecked = List(sessionChecked.size) { false }
                            genderChecked = List(genderChecked.size) { false }
                            religionChecked = List(religionChecked.size) { false }
                            districtChecked = List(districtChecked.size) { false }
                            categoryChecked = List(categoryChecked.size) { false }


                        }
                    ) {
                        Text(
                            //if (isZh) "清除全部" else "Clear All"
                                stringResource(id = R.string.btn_clear_all)
                        )
//                        stringResource(id = R.string.desc_search_icon)
                    }

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
                            selectedData["religion"] = religionList.filterIndexed { i, _ -> religionChecked[i] }
                            selectedData["category"] = categoryList.filterIndexed { i, _ -> categoryChecked[i] }
                            onConfirm(selectedData)

                        }

                    ) {
                        Text(if (isZh) "搜尋" else "Search")
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
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp) // 整個組件與上下內容的間距
                        .clickable { isSessionExpanded = !isSessionExpanded } // 點擊整塊區域觸發
                ) {


                    // context
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 8.dp), // 讓文字與線條有呼吸空間
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SectionHeader(if(isZh) "授課時間" else "Session")

                        Icon(
                            imageVector = if (isSessionExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // line
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                }
            }
            if (isSessionExpanded)
            itemsIndexed(sessionList) { index, label ->
                FilterRow(label, sessionChecked[index]) { isChecked ->
                    sessionChecked = sessionChecked.toMutableList().apply { this[index] = isChecked }
                }
            }

            // Genders
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp) // 整個組件與上下內容的間距
                        .clickable { isGenderExpanded = !isGenderExpanded } // 點擊整塊區域觸發
                ) {


                    // context
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 8.dp), // 讓文字與線條有呼吸空間
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SectionHeader(if(isZh) "學生性別" else "Gender")

                        Icon(
                            imageVector = if (isGenderExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // line
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                }
            }
            if(isGenderExpanded)
            itemsIndexed(genderList) { index, label ->
                FilterRow(label, genderChecked[index]) { isChecked ->
                    genderChecked = genderChecked.toMutableList().apply { this[index] = isChecked }
                }
            }

            // Districts
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp) // 整個組件與上下內容的間距
                        .clickable { isDistrictExpanded = !isDistrictExpanded } // 點擊整塊區域觸發
                ) {
                    // line
//                    HorizontalDivider(
//                        thickness = 1.dp,
//                        color = Color.Black.copy(alpha = 0.6f)
//                    )

                    // context
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 8.dp), // 讓文字與線條有呼吸空間
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SectionHeader(if(isZh) "地區" else "District")

                        Icon(
                            imageVector = if (isDistrictExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // line
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                }
            }
        if(isDistrictExpanded)
            itemsIndexed(districtList) { index, label ->
                FilterRow(label, districtChecked[index]) { isChecked ->
                    districtChecked = districtChecked.toMutableList().apply { this[index] = isChecked }
                }
            }

        //Religion
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp) // 整個組件與上下內容的間距
                        .clickable { isReligionExpanded = !isReligionExpanded } // 點擊整塊區域觸發
                ) {
//                    // line
//                    HorizontalDivider(
//                        thickness = 1.dp,
//                        color = Color.Black.copy(alpha = 0.6f)
//                    )

                    // context
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 8.dp), // 讓文字與線條有呼吸空間
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                         SectionHeader(if(isZh) "宗教" else "Religion")

                        Icon(
                            imageVector = if (isReligionExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // line
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                }
            }
            if (isReligionExpanded)
            itemsIndexed(religionList) { index, label ->
                FilterRow(label, religionChecked[index]) { isChecked ->
                    religionChecked = religionChecked.toMutableList().apply { this[index] = isChecked }
                }
            }
            //Category
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp) // 整個組件與上下內容的間距
                        .clickable { isCategoryExpanded = !isCategoryExpanded } // 點擊整塊區域觸發
                ) {

                    // context
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 8.dp), // 讓文字與線條有呼吸空間
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                         SectionHeader(if(isZh) "類別" else "Category")

                        Icon(
                            imageVector = if (isCategoryExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // line
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                }
            }


            if (isCategoryExpanded)
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
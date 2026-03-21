package com.example.comp_3132sef.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.ui.school.FilterViewModel
//import com.example.comp_3132sef.filteredListIsEmpty
//import com.example.comp_3132sef.getNameListItemsToLists
//import com.example.comp_3132sef.mappingToChooseResult
import com.example.comp_3132sef.ui.school.SchoolViewModel
import com.example.comp_3132sef.ui.school.SearchSchoolViewModel
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchBarCompose(viewModel:SearchSchoolViewModel, viewModel2: SchoolViewModel,isZh:Boolean) {
    val query by viewModel.searchQuery.collectAsState()
    val schools2 by viewModel.searchResults.collectAsState()
    val selectedSchool by viewModel2.selectedSchool.collectAsState()
    var searchActive by remember { mutableStateOf(false) }
    var btnActive by remember { mutableStateOf(false) }
    val schoolsNameList = mutableListOf<String>()
    getNameListItemsToLists(schoolsNameList, schools2, !isZh)
    val filteredList = schoolsNameList
    val viewModel3: FilterViewModel = viewModel()
    if(btnActive) {
        FilterPanelApp(
            viewModel = viewModel3,
            isZh = isZh,
            onBack = {
                // 這裡如果只是想關閉 Filter 介面，改用這個：
                btnActive = false
                // 如果是要退回上一個手機頁面，才用：navController.popBackStack()
            },
            onConfirm = { selectedFilters ->
                // 1. 執行你的搜尋邏輯
                println("User selected: $selectedFilters")
                // 2. 關閉篩選面板
                btnActive = false
                // 3. (選填) 如果想跳到結果頁：
                // navController.navigate("result_screen")
            }
        )
    }else{

    }


    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
        Box(){
            Row(){
        SearchBar(
            modifier = Modifier
//                .align(Alignment.TopStart)
                .padding(top = if (searchActive) 0.dp else 8.dp)
                .fillMaxWidth(if (searchActive) 1f else 0.8f) ,
            query = query,
            onQueryChange = {
                viewModel.onSearchQueryChange(it)  },
            onSearch = { searchActive = false },
            active = searchActive,
            onActiveChange = { searchActive = it },
            placeholder = { Text("搜尋...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchActive) {
                    IconButton(onClick = { if (!query.isNotEmpty())   searchActive = false }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            }
        )

        {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
                if (filteredList.isEmpty()) {
                    // 正確調用擴充函式
                    filteredListIsEmpty()
                }else{
                    items(schools2) { school ->

                        val displayName =
                            if (isZh) (school.chineseName ?: school.englishName)
                            else school.englishName
                        ListItem(
                            headlineContent = { Text(displayName) },
                            supportingContent = { Text("點擊選擇 $displayName") },
                            leadingContent = { Icon(Icons.Default.History, contentDescription = null) },
                            modifier = Modifier.clickable {

                                mappingToChooseResult(selectedSchool,viewModel2,school)

                                searchActive = false

                            }

                        )


                    }
                }

            }
        }
        Button(onClick = {
            btnActive = true },
                modifier = Modifier
                    .padding(top = 16.dp)
            ,


        ){
            Text("btn")
        }

        }
        }

    }
}

fun getNameListItemsToLists(inputList: MutableList<String>, list: List<SchoolEntity>, isEnglish: Boolean) {
    // 使用 forEach 遍歷 schools2，並將名稱加入 inputList
    if(isEnglish){
        list.forEach { school ->
            inputList.add(school.englishName)
        }
    }else{
        list.forEach { school ->
            inputList.add(school.chineseName ?: school.englishName)
        }
    }
}

fun LazyListScope.filteredListIsEmpty() {
    item {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "找不到結果",
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

fun mappingToChooseResult(selectedSchool: SchoolEntity?,viewModel2: SchoolViewModel,school: SchoolEntity){

    if (selectedSchool != null) {
        viewModel2.closeSchoolMap()}
    viewModel2.openSchoolMap(school)
}

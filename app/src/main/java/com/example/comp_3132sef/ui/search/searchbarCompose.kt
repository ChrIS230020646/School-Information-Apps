package com.example.comp_3132sef.ui.search

import android.app.Activity
import android.content.Intent
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp_3132sef.MainActivity
import com.example.comp_3132sef.R
import com.example.comp_3132sef.SearchResultActivity
import com.example.comp_3132sef.data.local.SchoolDataHolder
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.ui.detail.SchoolDetailScreen
import com.example.comp_3132sef.ui.school.FilterViewModel
//import com.example.comp_3132sef.filteredListIsEmpty
//import com.example.comp_3132sef.getNameListItemsToLists
//import com.example.comp_3132sef.mappingToChooseResult
import com.example.comp_3132sef.ui.school.SchoolViewModel
import com.example.comp_3132sef.ui.school.SearchSchoolViewModel
import kotlin.collections.forEach

val schoolsNameList = mutableListOf<String>()
//getNameListItemsToLists(schoolsNameList, schools2, !isZh)
val filteredList = schoolsNameList
//val viewModel3: FilterViewModel = viewModel()
//var currencySelectedFilters by remember { mutableStateOf<Map<String, List<String>>>(emptyMap()) }
//val filterCheckedSet = rememberFilterCheckedSet(viewModel3, isZh)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchBarCompose(viewModel:SearchSchoolViewModel,
                     searchActive:Boolean,
                     btnActive:Boolean,

                     onClickResult:Boolean,
                     selectSchool: SchoolEntity?,

                     isZh:Boolean,
                     onSearchActiveChange: (Boolean) -> Unit,
                     onfilterActiveChange: (Boolean) -> Unit,
                     onClickResultChange: (Boolean) -> Unit,
                     onSelectSchoolChange: (SchoolEntity) -> Unit,
                     onSearch: () -> Unit) {
    val query by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current

//    val schoolsNameList = mutableListOf<String>()
//    if(searchActive)
    getNameListItemsToLists(schoolsNameList, searchResults, !isZh)
    val viewModel3: FilterViewModel = viewModel()
    var currencySelectedFilters by remember { mutableStateOf<Map<String, List<String>>>(emptyMap()) }
    val filterCheckedSet = rememberFilterCheckedSet(viewModel3, isZh)


    if(btnActive) {
        FilterPanelApp(
//            viewModel = viewModel3,
            filterCheckedSet=filterCheckedSet,
            selectSet=currencySelectedFilters,
            onBack = {

                onfilterActiveChange(false)
            },
            onConfirm = { selectedFilters ->
                // 1. 執行你的搜尋邏輯
                println("User selected: $selectedFilters")

                viewModel.onUpdateFilter(selectedFilters)
                onfilterActiveChange(false)
                SchoolDataHolder.currencySelectedFilters=selectedFilters
//                viewModel.onSearchQueryChange(SchoolDataHolder.query)
                viewModel.onUpdateFilter(SchoolDataHolder.currencySelectedFilters)

                SchoolDataHolder.isZh=isZh
                if(context.equals(MainActivity::class.java))
                    (context as? Activity)?.finish()
                val intent = Intent(context, SearchResultActivity::class.java).apply {
                    // 加入這個 Flag
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            },clarAll={
                currencySelectedFilters = emptyMap()
            }

        )
    }


    Box(Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(top = 100.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//        }
        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd){
            Row(){
                if(!btnActive)
                SearchBar(
            modifier = Modifier
//                .align(Alignment.TopStart)
                .padding(top = if (searchActive) 0.dp else 4.dp)
//                .fillMaxWidth(if (searchActive) 1f else 0.93f)
                ,
            query = query,
            onQueryChange = {
                viewModel.onSearchQueryChange(it)
                 },
            onSearch = {
//                searchActive = false
                onSearchActiveChange(false)
                       },
            active = searchActive,
            onActiveChange = onSearchActiveChange,
//            onActiveChange = {searchActive = it  },
//            onActiveChange = { searchActive = it },
            placeholder = { Text(text = stringResource(id = R.string.search_placeholder)) },
            leadingIcon = {
                if (searchActive)
                IconButton(onClick = {
                    onSearchActiveChange(false)

                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                if (!searchActive)
                Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                Row(){
                    if (!searchActive)
                IconButton(
                    onClick = {
                        keyboardController?.hide()
                        onfilterActiveChange(true) },
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList, // 或者用 Icons.Default.Tune
                        contentDescription = R.string.filter.toString(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                if (searchActive) {

                    IconButton(onClick = {
                        // 執行搜尋邏輯
                        println("搜尋內容: $query")
                        keyboardController?.hide() // 隱藏鍵盤
                        SchoolDataHolder.query= query
//                        SchoolDataHolder.currencySelectedFilters=currencySelectedFilters
                        onSearch()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription =R.string.btn_search_confirm.toString(),
                            //contentDescription = R.string.btn_filter_desc.toString()
                        )
                    }

                    IconButton(onClick = {
//                        if (!query.isNotEmpty())
//                        searchActive = false
                        viewModel.onSearchQueryChange("")
                        onSearchActiveChange(false)
                    }
                    ) {

                        Icon(Icons.Default.Close, contentDescription = null)
                    }

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
                    items(searchResults) { school ->

                        val displayName =
                            if (isZh) (school.chineseName ?: school.englishName)
                            else school.englishName
                        ListItem(
                            headlineContent = { Text(displayName) },
                            supportingContent = { Text("點擊選擇 $displayName") },
                            leadingContent = { Icon(Icons.Default.History, contentDescription = null) },
                            modifier = Modifier.clickable {
                                print(school)
                                onSelectSchoolChange(school)
                                onClickResultChange(true)
                                onSearchActiveChange(false)
//                                searchActive = false

                            }

                        )


                    }
                }

            }
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
                R.string.search_no_results.toString(),
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

//@Composable
//fun mappingToChooseResult(selectedSchool: SchoolEntity?, viewModel2: SchoolViewModel, school: SchoolEntity){
//
////    if (selectedSchool != null) {
////        viewModel2.closeSchoolMap()}
////    viewModel2.openSchoolMap(school)
//    SchoolDetailScreen(
//        school = school!!,
//        onBack = { viewModel2.closeSchoolMap() })
//
//}

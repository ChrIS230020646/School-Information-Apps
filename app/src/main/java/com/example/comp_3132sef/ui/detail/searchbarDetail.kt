package com.example.comp_3132sef.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import com.example.comp_3132sef.filteredListIsEmpty
import com.example.comp_3132sef.getNameListItemsToLists
import com.example.comp_3132sef.mappingToChooseResult
import com.example.comp_3132sef.ui.school.SchoolViewModel
import com.example.comp_3132sef.ui.school.SearchSchoolViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchBarCompose(viewModel:SearchSchoolViewModel, viewModel2: SchoolViewModel,isZh:Boolean) {
    val query by viewModel.searchQuery.collectAsState()
    val schools2 by viewModel.searchResults.collectAsState()
    val selectedSchool by viewModel2.selectedSchool.collectAsState()
    var active by remember { mutableStateOf(false) }
    val schoolsNameList = mutableListOf<String>()
    getNameListItemsToLists(schoolsNameList, schools2, !isZh)
    val filteredList = schoolsNameList
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }

        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = if (active) 0.dp else 8.dp),
            query = query,
            onQueryChange = {
                viewModel.onSearchQueryChange(it)  },
            onSearch = { active = false },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text("搜尋...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (active) {
                    IconButton(onClick = { if (!query.isNotEmpty())   active = false }) {
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

                                active = false

                            }

                        )


                    }
                }

            }
        }

    }
}
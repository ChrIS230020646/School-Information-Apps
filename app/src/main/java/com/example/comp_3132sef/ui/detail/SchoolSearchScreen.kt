package com.example.comp_3132sef.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp_3132sef.R
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.ui.school.SearchSchoolViewModel
import okhttp3.Address
import java.util.Locale

@Composable
fun SchoolSearchScreen(viewModel: SearchSchoolViewModel) {
    val query by viewModel.searchQuery.collectAsState()
    val schools by viewModel.searchResults.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "學校資料搜索",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 搜索欄
        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.onQueryChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("輸入名稱、地址或電話...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 結果列表
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(schools) { school ->
                SchoolCard(school)
            }
        }

        if (schools.isEmpty() && query.isNotEmpty()) {
            Text("找不到相關學校資料", modifier = Modifier.padding(top = 20.dp), color = Color.Gray)
        }
    }
}
@Composable
fun SchoolCard(school: SchoolEntity) {
    val isZh = Locale.getDefault().language == "zh"
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            val displayName1 =
                if (isZh) (school.chineseName ?: school.englishName)
                else school.englishName
            val displayName2 =
                if (isZh) (school.englishName ?: school.chineseName)
                else school.chineseName
            displayName1.let {
                if (it != null) {
                    Text(text = it, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
            if (displayName2 != null) {
                Text(text = displayName2, fontSize = 14.sp, color = Color.Gray)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            val address =
                if (isZh) (school.chineseAddress ?: school.englishAddress)
                else school.englishAddress
            if (address != null) {
                DetailRow(stringResource(R.string.Address), address)
            }
            DetailRow(stringResource(R.string.Phone_No), school.telephone!!)
            val district = if (isZh) (school.chineseDistrict ?: school.district)
            else school.district
            if (district != null) {
                DetailRow(stringResource(R.string.District), district)
            }
            val religion = if (isZh) (school.chineseReligion ?: school.religion)
            else school.religion
            if (religion != null) {
                DetailRow(stringResource(R.string.Religion), religion)
            }
            val Category = if (isZh) (school.chineseCategory ?: school.englishCategory)
        else school.englishCategory
            val Session = if (isZh) (school.chineseSession ?: school.session)
            else school.session
            DetailRow(stringResource(R.string.Category), "${Category} (${Session})")

            Text(
                text = school.website!!,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(text = "$label: ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Text(text = value, fontSize = 14.sp)
    }
}

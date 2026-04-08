package com.example.comp_3132sef

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp_3132sef.ui.school.SchoolViewModel
import com.example.comp_3132sef.ui.theme.COMP_3132SEFTheme
import com.example.comp_3132sef.ui.detail.SchoolDetailScreen
import java.util.Locale

import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.platform.LocalConfiguration

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import androidx.room.util.getColumnIndex
import com.example.comp_3132sef.data.local.SchoolDataHolder
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.ui.detail.DetailRow
import com.example.comp_3132sef.ui.school.SearchSchoolViewModel
import com.example.comp_3132sef.ui.search.searchBarCompose


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: SchoolViewModel = viewModel()
            val SearchSchoolViewModel:SearchSchoolViewModel= viewModel()
            Surface(color = MaterialTheme.colorScheme.background) {
            COMP_3132SEFTheme {

                // 使用更安全的判斷方式 (Use safer null checks)
                val queryText = SchoolDataHolder.query?: ""
                val filters = SchoolDataHolder.currencySelectedFilters ?: emptyMap()
                SearchSchoolViewModel.onUpdateFilter(filters)


                Surface(modifier = Modifier.fillMaxSize()) {

                        searchResultScreen(viewModel,SearchSchoolViewModel)

                }
            }
        }
        }
    }
}
@Composable
fun SchoolCard(school: SchoolEntity,onClick: ()->Unit) {
    val isZh = SchoolDataHolder.isZh
    val displayName1 =
        if (isZh) (school.chineseName ?: school.englishName)
        else school.englishName
    val displayName2 =
        if (isZh) (school.englishName ?: school.chineseName)
        else school.chineseName
    val address =
        if (isZh) (school.chineseAddress ?: school.englishAddress)
        else school.englishAddress
    val district = if (isZh) (school.chineseDistrict ?: school.district)
    else school.district
    val religion = if (isZh) (school.chineseReligion ?: school.religion)
    else school.religion
    val Category = if (isZh) (school.chineseCategory ?: school.englishCategory)
    else school.englishCategory
    val Session = if (isZh) (school.chineseSession ?: school.session)
    else school.session
    val gender = if (isZh) (school.chineseStudentsGender ?: school.studentsGender)
else school.studentsGender
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable {onClick() },
        elevation = CardDefaults.cardElevation(4.dp)


    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Box(modifier = Modifier.padding(end = 16.dp)) {
            displayName1.let {
                if (!(it.isEmpty())) {
                    Text(text = it, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
            }
            if (displayName2 != null) {
                Text(text = displayName2, fontSize = 14.sp, color = Color.Gray)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            if (address != null) {
                DetailRow(stringResource(R.string.Address), address)
            }
            DetailRow(stringResource(R.string.Phone_No), school.telephone ?: "N/A")

            if (district != null) {
                DetailRow(stringResource(R.string.District), district)
            }

            if (religion != null) {
                DetailRow(stringResource(R.string.Religion), religion)
            }
            if (gender != null) {
                DetailRow(stringResource(R.string.gender), gender)
            }
            DetailRow(stringResource(R.string.Category), "${Category ?: ""} (${Session ?: ""})")

            school.website?.let { url ->
                if (url.isNotEmpty()) {
                    Text(
                        text = url,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
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

@Composable
fun searchResultScreen(
    viewModel: SchoolViewModel ,
    searchSchoolViewModel:SearchSchoolViewModel,
//    back: () -> Unit
) {
    val TAG = "DebugMove" // 定義 Log 標籤
    var isZh = SchoolDataHolder.isZh
    var searchActive by remember { mutableStateOf(false) }
    var filterActive by remember { mutableStateOf(false) }
    var onClickResult by remember { mutableStateOf(false) }
    var selectSchool by remember { mutableStateOf<SchoolEntity?>(null) }
    val context = LocalContext.current
    var currentLocale by remember(SchoolDataHolder.isZh) {
        mutableStateOf(if (SchoolDataHolder.isZh) Locale("zh", "HK") else Locale.ENGLISH)
    }
    val configuration = Configuration(LocalConfiguration.current).apply {
        setLocale(currentLocale)
    }
    val localizedContext = context.createConfigurationContext(configuration)

    CompositionLocalProvider(
        LocalConfiguration provides configuration,
        LocalContext provides localizedContext
    ) {
        LaunchedEffect(onClickResult) {
            if (onClickResult) {
                if (selectSchool != null) {
                    Log.d(TAG, "selectSchool ID: ${selectSchool!!.id}")
                    try {
                        SchoolDataHolder.selectedSchool = selectSchool
                        SchoolDataHolder.isZh = isZh

                        val intent = Intent(context, SearchResultActivity::class.java)

                        context.startActivity(intent)

                        Log.d(TAG, "startActivity run")
                    } catch (e: Exception) {
                        Log.e(TAG, "[warn] can't jump: ${e.message}")
                    } finally {
                        onClickResult = false // 重置狀態
                        searchActive = false
                    }
                } else {
                    Log.w(TAG, "[warn] selectSchool null")
                    onClickResult = false
                }
            }
        }

        LaunchedEffect(SchoolDataHolder.query, SchoolDataHolder.currencySelectedFilters) {
            Log.d(TAG, "Initializing Search Results with Holder Data")
            searchSchoolViewModel.onSearchQueryChange(SchoolDataHolder.query)
            searchSchoolViewModel.onUpdateFilter(SchoolDataHolder.currencySelectedFilters)
        }

        // 觀察結果
        val searchResults by searchSchoolViewModel.searchResults.collectAsState()
        val favorites by viewModel.favorites.collectAsState()
        Box(
            modifier = Modifier
//                .height((16 * 25).dp)
                .padding(16.dp,)
                .padding(top = 120.dp, start = 24.dp, end = 4.dp),

            contentAlignment = Alignment.Center,


            ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
//            .statusBarsPadding()
                ,

                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp) // 卡片間距
            ) {
                items(searchResults) { school ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // 點擊卡片跳轉詳情
                        SchoolCard(school, onClick = {
                            selectSchool = school
                            onClickResult = true
                        })


                        val favKey = school.englishName ?: ""
                        IconButton(
                            onClick = { viewModel.toggleFavorite(favKey) },
                            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                        ) {
                            Icon(
                                imageVector = if (favorites.contains(favKey)) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = "Favorite",
                                tint = if (favorites.contains(favKey)) Color(0xFFFFC107) else Color.Gray
                            )
                        }
                    }
                }
            }
        }
        Column(modifier = Modifier.fillMaxSize()) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()

            ) {
                if (!searchActive && !filterActive)
                    IconButton(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            searchSchoolViewModel.onSearchQueryChange("")
                            SchoolDataHolder.currencySelectedFilters= emptyMap()

                            val intent = Intent(context, MainActivity1::class.java)
                            context.startActivity(intent)
                            (context as? Activity)?.finish()
                        }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }

                searchBarCompose(
                    searchSchoolViewModel,
                    searchActive,
                    filterActive,
                    onClickResult,
                    selectSchool,
                    isZh,
                    onSearchActiveChange = { searchActive = it },
                    onfilterActiveChange = { filterActive = it },
                    onClickResultChange = { onClickResult = it },
                    onSelectSchoolChange = { selectSchool = it },
                    onSearch = {
                        filterActive = false
                        SchoolDataHolder.isZh = isZh
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        (context as? Activity)?.finish()
                    }
                )

            }
        }

    }

}

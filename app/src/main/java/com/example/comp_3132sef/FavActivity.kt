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
import java.util.Locale

import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.platform.LocalConfiguration

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.comp_3132sef.data.local.SchoolDataHolder
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.ui.school.SearchSchoolViewModel
import com.example.comp_3132sef.ui.search.searchBarCompose


class FavActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val TAG = "DebugMove" // 定義 Log 標籤

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

                val hasFilter = filters.isNotEmpty()
                val hasQuery = queryText.isNotBlank()

                Surface(modifier = Modifier.fillMaxSize()) {
                    if (hasFilter || hasQuery) {
                        Log.d("DebugMove", "Mode: Search Result (Q: '$queryText', F: ${filters.size})")
                        searchResultScreen2(viewModel,SearchSchoolViewModel)
                    } else {
                        Log.d("DebugMove", "Mode: Main List (Displaying all items)")
//                        MainScreen(viewModel)
                        searchResultScreen2(viewModel,SearchSchoolViewModel)
                    }
                }
            }
            }
        }
    }
}
@Composable
fun SchoolCard2(school: SchoolEntity,onClick: ()->Unit) {
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
                DetailRow2(stringResource(R.string.Address), address)
            }
            DetailRow2(stringResource(R.string.Phone_No), school.telephone!!)

            if (district != null) {
                DetailRow2(stringResource(R.string.District), district)
            }

            if (religion != null) {
                DetailRow2(stringResource(R.string.Religion), religion)
            }

            DetailRow2(stringResource(R.string.Category), "${Category} (${Session})")

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
fun DetailRow2(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(text = "$label: ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Text(text = value, fontSize = 14.sp)
    }
}

@Composable
fun searchResultScreen2(
    viewModel: SchoolViewModel ,
    searchSchoolViewModel:SearchSchoolViewModel,
//    back: () -> Unit
){

    val isDarkTheme = isSystemInDarkTheme()
    val headerBackgroundColor = if (isDarkTheme) Color.Gray else Color.DarkGray
    val headerTextColor = Color.White
    val favoritesInfo by viewModel.favoriteSchoolEntities.collectAsState()
    val TAG = "DebugMove" // 定義 Log 標籤

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
                Log.d(TAG, "準備跳轉！目標 ID: ${selectSchool!!.id}")
                try {
                    SchoolDataHolder.selectedSchool = selectSchool
//                    SchoolDataHolder.isZh=isZh
                    onClickResult=false
                    val intent = Intent(context, SearchResultActivity::class.java)

                    context.startActivity(intent)
                    Log.d(TAG, "startActivity 已執行")
                } catch (e: Exception) {
                    Log.e(TAG, "跳轉失敗: ${e.message}")
                } finally {
//                    onClickResult = false // 重置狀態
//                    searchActive=false
                }
            } else {
                Log.w(TAG, "onClickResult 為 true 但 selectSchool 是空的，跳轉取消")
                onClickResult = false
            }
        }
    }

    // 使用 LaunchedEffect 確保只在進入此頁面時設定一次 ViewModel 狀態
    LaunchedEffect(SchoolDataHolder.query, SchoolDataHolder.currencySelectedFilters) {
        Log.d(TAG, "Initializing Search Results with Holder Data")
        searchSchoolViewModel.onSearchQueryChange(SchoolDataHolder.query)
        searchSchoolViewModel.onUpdateFilter(SchoolDataHolder.currencySelectedFilters)
    }

    // 觀察結果
    val searchResults by searchSchoolViewModel.searchResults.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    Box(modifier = Modifier
//                .height((16 * 25).dp)
        .padding(16.dp,)
        .padding(top = 120.dp, start =24.dp,end =4.dp),

        contentAlignment = Alignment.Center,


        ){
        LazyColumn(
            modifier = Modifier.fillMaxSize()
//            .statusBarsPadding()
            ,

            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // 卡片間距
        ) {
            items(favoritesInfo) { school ->
                Box(modifier = Modifier.fillMaxWidth()) {

                    SchoolCard2(school, onClick = {
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
        }}
    Column(modifier = Modifier.fillMaxSize()) {

        // --- 頂部欄區域 ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding() // 避免被狀態欄擋住
                .background(headerBackgroundColor),
                 verticalAlignment = Alignment.CenterVertically
//                .padding(horizontal = 4.dp),
//            verticalAlignment = Alignment.CenterVertically
        ) {
//            if(!searchActive && !filterActive )
            IconButton(onClick = { (context as? Activity)?.finish() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "返回",
                tint = Color.White)
                }
            Text(
                text = stringResource(R.string.favourites),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp),
                color = headerTextColor,

                )

//        searchBarCompose(
//            searchSchoolViewModel,
//            searchActive,
//            filterActive,
//            onClickResult,
//            selectSchool,
//            isZh,
//            onSearchActiveChange = { searchActive = it },
//            onfilterActiveChange = { filterActive = it },
//            onClickResultChange = { onClickResult = it },
//            onSelectSchoolChange = { selectSchool = it },
//            onSearch = {
//                // 執行搜尋邏輯
//            }
//        )

}

    }}

}


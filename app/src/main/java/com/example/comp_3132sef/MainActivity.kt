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
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import androidx.room.util.getColumnIndex
import com.example.comp_3132sef.data.local.SchoolDataHolder
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.ui.school.SearchSchoolViewModel
import com.example.comp_3132sef.ui.search.searchBarCompose


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val TAG = "DebugMove" // 定義 Log 標籤

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: SchoolViewModel = viewModel()
            val SearchSchoolViewModel:SearchSchoolViewModel= viewModel()
            COMP_3132SEFTheme {

                // 使用更安全的判斷方式 (Use safer null checks)
                val queryText = SchoolDataHolder.query?: ""
                val filters = SchoolDataHolder.currencySelectedFilters ?: emptyMap()



                Surface(modifier = Modifier.fillMaxSize()) {
//                    if (hasFilter || hasQuery) {
//                        Log.d("DebugMove", "Mode: Search Result (Q: '$queryText', F: ${filters.size})")
                        searchResultScreen(viewModel,SearchSchoolViewModel)
//                    } else {
//                        Log.d("DebugMove", "Mode: Main List (Displaying all items)")
////                        MainScreen(viewModel)
//                        searchResultScreen(viewModel,SearchSchoolViewModel)
//                    }
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
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable {onClick() },
        elevation = CardDefaults.cardElevation(4.dp)


    ) {
        Column(modifier = Modifier.padding(16.dp)) {


            displayName1.let {
                if (!(it.isEmpty())) {
                    Text(text = it, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
            if (displayName2 != null) {
                Text(text = displayName2, fontSize = 14.sp, color = Color.Gray)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            if (address != null) {
                DetailRow(stringResource(R.string.Address), address)
            }
            DetailRow(stringResource(R.string.Phone_No), school.telephone!!)

            if (district != null) {
                DetailRow(stringResource(R.string.District), district)
            }

            if (religion != null) {
                DetailRow(stringResource(R.string.Religion), religion)
            }

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

@Composable
fun searchResultScreen(
    viewModel: SchoolViewModel ,
    searchSchoolViewModel:SearchSchoolViewModel,
//    back: () -> Unit
){
    val TAG = "DebugMove" // 定義 Log 標籤
    var isZh = SchoolDataHolder.isZh
    var searchActive by remember { mutableStateOf(false) }
    var filterActive by remember { mutableStateOf(false) }
    var onClickResult by remember { mutableStateOf(false) }
    var selectSchool by remember { mutableStateOf<SchoolEntity?>(null) }
    val context = LocalContext.current
    LaunchedEffect(onClickResult) {
        if (onClickResult) {
            if (selectSchool != null) {
                Log.d(TAG, "準備跳轉！目標 ID: ${selectSchool!!.id}")
                try {
                    SchoolDataHolder.selectedSchool = selectSchool
                    SchoolDataHolder.isZh=isZh

                    val intent = Intent(context, SearchResultActivity::class.java)

                    context.startActivity(intent)
                    Log.d(TAG, "startActivity 已執行")
                } catch (e: Exception) {
                    Log.e(TAG, "跳轉失敗: ${e.message}")
                } finally {
                    onClickResult = false // 重置狀態
                    searchActive=false
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
            items(searchResults) { school ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    // 點擊卡片跳轉詳情
                    SchoolCard(school, onClick = {
                        selectSchool = school
                        onClickResult = true
                    })

                    // 收藏按鈕 (疊加在右上方或放在 Row 裡)
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
//                .padding(horizontal = 4.dp),
//            verticalAlignment = Alignment.CenterVertically
        ) {
            if(!searchActive && !filterActive )
            IconButton(onClick = { (context as? Activity)?.finish() }) {
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
                SchoolDataHolder.isZh=isZh
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        )

}

    }

//    Box(modifier = Modifier.fillMaxSize()) {
//        // 1. Your main screen content (List, Column, etc.)
//
//        // 2. The FAB placed manually
//        FloatingActionButton(
//            onClick = {
//                try {
////                    // 1. Create the Intent
////                    val intent = Intent(context, SearchActivity::class.java).apply {
////                        putExtra("EXTRA_DATA", "Hello from Compose!")
////                    }
//                    (context as? Activity)?.finish()
//                    // 2. Try to start the Activity
////                    context.startActivity(intent)
//
//                } catch (e: Exception) {
//                    // 3. Handle the "problem"
//                    // This logs it to your Logcat for debugging
//                    Log.e("NavigationError", "Failed to launch HomeActivity", e)
//
//                    // This shows a popup to the user so they know something went wrong
//                    Toast.makeText(
//                        context,
//                        "Error: ${e.localizedMessage}",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//
//            },
//            modifier = Modifier
//                .align(Alignment.TopEnd) // Put it in the top right!
//                .padding(top = 40.dp, end = 16.dp)
//        ) {
//            Icon(Icons.Default.ArrowBack, contentDescription = "返回")
//        }
//    }
}


//@Composable
//fun MainScreen(
//    viewModel: SchoolViewModel,
//
//) {
//
//    val selectedSchool by viewModel.selectedSchool.collectAsState()
//
//    if (selectedSchool != null) {
//        Column {
//            SchoolDetailScreen(
//                school = selectedSchool!!,
//                onBack = { viewModel.closeSchoolMap() }
//            )
//        }
//    } else {
//        SchoolListScreen(viewModel)
//    }
//}


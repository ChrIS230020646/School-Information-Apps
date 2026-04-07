package com.example.comp_3132sef

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp_3132sef.data.local.SchoolDataHolder
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.ui.detail.FavItemList
import com.example.comp_3132sef.ui.school.FilterViewModel
import com.example.comp_3132sef.ui.school.SchoolViewModel
import com.example.comp_3132sef.ui.school.SearchSchoolViewModel
import com.example.comp_3132sef.ui.search.FilterPanelApp
import com.example.comp_3132sef.ui.search.rememberFilterCheckedSet
import java.util.Locale

class MainActivity1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val searchVm: SearchSchoolViewModel = viewModel()
            val schoolVm: SchoolViewModel = viewModel()

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchBarScreen(searchVm, schoolVm)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarScreen(viewModel: SearchSchoolViewModel, viewModel2: SchoolViewModel) {
    val TAG = "LocaleDebug"
    // 這是原始的 Activity Context，用於 startActivity 確保不崩潰
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // --- 語系狀態管理 ---
    var currentLocale by remember(SchoolDataHolder.isZh) {
        mutableStateOf(if (SchoolDataHolder.isZh) Locale("zh", "HK") else Locale.ENGLISH)
    }

    val configuration = Configuration(LocalConfiguration.current).apply {
        setLocale(currentLocale)
    }
    // 這是用於顯示語系的 Context
    val localizedContext = context.createConfigurationContext(configuration)

    // --- UI 狀態 ---
    var filterActive by remember { mutableStateOf(false) }
    var onClickResult by remember { mutableStateOf(false) }
    var selectSchool by remember { mutableStateOf<SchoolEntity?>(null) }
    var currencySelectedFilters by remember { mutableStateOf<Map<String, List<String>>>(emptyMap()) }

    CompositionLocalProvider(
        LocalConfiguration provides configuration,
        LocalContext provides localizedContext
    ) {
        // 監聽單個學校跳轉 (SearchResultActivity)
        LaunchedEffect(onClickResult) {
            if (onClickResult && selectSchool != null) {
                SchoolDataHolder.selectedSchool = selectSchool
                // 核心修復：使用原始 context 跳轉
                context.startActivity(Intent(context, SearchResultActivity::class.java))
                onClickResult = false
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {

            // 1. 背景層：收藏卡片 (置中偏上)
            Row(
                verticalAlignment = BiasAlignment.Vertical(0.1f),
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .shadow(8.dp, RoundedCornerShape(12.dp))
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .clickable { MoveToFavPage(context) }
                        .fillMaxWidth(0.85f)
                        .height(540.dp)
                        .padding(16.dp),
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.favourites),
                                style = MaterialTheme.typography.titleMedium
                            )
                            IconButton(onClick = { MoveToFavPage(context) }) {
                                Icon(Icons.Filled.Star, contentDescription = null, tint = Color.Gray)
                            }
                        }
                        Box(modifier = Modifier.padding(top = 16.dp)) {
                            FavItemList(viewModel2)
                        }
                    }
                }
            }

            // 2. 前景層：控制區域 (頂部操作列)
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

                // 頂部橫列：左邊切換語系，右邊功能按鈕
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 左上角：語系切換
                    TextButton(onClick = {
                        val targetIsZh = !SchoolDataHolder.isZh
                        SchoolDataHolder.isZh = targetIsZh
                        // 這會觸發 remember(SchoolDataHolder.isZh) 重新組合
                    }) {
                        Text(text = stringResource(id = R.string.language))
                    }

                    // 右上角：搜尋與篩選圖標
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (!filterActive) {
                            IconButton(onClick = {
                                val intent = Intent(context, MainActivity::class.java)
                                (context as? Activity)?.finish()
                                context.startActivity(intent)
                            }) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = stringResource(id = R.string.search),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(onClick = {
                                keyboardController?.hide()
                                filterActive = true
                            }) {
                                Icon(
                                    Icons.Default.FilterList,
                                    contentDescription = stringResource(id = R.string.filter),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                // 推開中間空間
                Spacer(modifier = Modifier.weight(1f))

                // 3. 篩選面板 (當開啟時)
                if (filterActive) {
                    val viewModel3: FilterViewModel = viewModel()
                    val filterCheckedSet = rememberFilterCheckedSet(viewModel3, SchoolDataHolder.isZh)

                    FilterPanelApp(
                        filterCheckedSet = filterCheckedSet,
                        selectSet = currencySelectedFilters,
                        onBack = { filterActive = false },
                        onConfirm = { _ ->
                            // 修復點：從 filterCheckedSet 獲取所有勾選的內容並存入 DataHolder
                            val selected = mapOf(
                                "sessions" to filterCheckedSet.getSelectedList("sessions"),
                                "districts" to filterCheckedSet.getSelectedList("districts"),
                                "genders" to filterCheckedSet.getSelectedList("genders"),
                                "religions" to filterCheckedSet.getSelectedList("religions"),
                                "categories" to filterCheckedSet.getSelectedList("categories")
                            )

                            SchoolDataHolder.currencySelectedFilters = selected
                            viewModel.onUpdateFilter(selected)
                            filterActive = false

                            // 重啟主頁以刷新列表內容
                            val intent = Intent(context, MainActivity::class.java)
                            (context as? Activity)?.finish()
                            context.startActivity(intent)
                        },
                        clarAll = {
                            currencySelectedFilters = emptyMap()
                            // 同時清空 DataHolder 內緩存的過濾條件
                            SchoolDataHolder.currencySelectedFilters = emptyMap()
                        }
                    )
                }
            }
        }
    }
}

fun MoveToFavPage(context: Context) {
    val intent = Intent(context, FavActivity::class.java)
    context.startActivity(intent)
}
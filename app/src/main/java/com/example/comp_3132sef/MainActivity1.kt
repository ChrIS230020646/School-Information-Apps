package com.example.comp_3132sef

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
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
import androidx.compose.material.icons.outlined.StarBorder
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
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // --- 語系狀態管理 (統一由 SchoolDataHolder 控制) ---
    var currentLocale by remember(SchoolDataHolder.isZh) {
        mutableStateOf(if (SchoolDataHolder.isZh) Locale("zh", "HK") else Locale.ENGLISH)
    }

    // 建立新的配置與 Context
    val configuration = Configuration(LocalConfiguration.current).apply {
        setLocale(currentLocale)
    }
    val localizedContext = context.createConfigurationContext(configuration)

    // --- 其他 UI 狀態 ---
    var searchActive by remember { mutableStateOf(false) }
    var filterActive by remember { mutableStateOf(false) }
    var onClickResult by remember { mutableStateOf(false) }
    var selectSchool by remember { mutableStateOf<SchoolEntity?>(null) }
    var currencySelectedFilters by remember { mutableStateOf<Map<String, List<String>>>(emptyMap()) }

    // 使用 Provider 注入配置，讓 stringResource(R.string.xxx) 生效
    CompositionLocalProvider(
        LocalConfiguration provides configuration,
        LocalContext provides localizedContext
    ) {
        // 監聽跳轉
        LaunchedEffect(onClickResult) {
            if (onClickResult && selectSchool != null) {
                SchoolDataHolder.selectedSchool = selectSchool
                context.startActivity(Intent(context, SearchResultActivity::class.java))
                onClickResult = false
                searchActive = false
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Background Content (Logo + Favourites Card)
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                // Using a lower bias (e.g., 0.3f) moves the whole stack down to avoid top icons
                verticalAlignment = BiasAlignment.Vertical(0.3f)
            ) {
                // THIS COLUMN IS THE KEY: It stacks the image on top of the card
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(0.75f) // The whole stack is 75% wide
                ) {
                    // THE IMAGE
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .size(180.dp) // Set your desired size
                            .padding(bottom = 24.dp) // Creates space between logo and card
                    )

                    // THE FAVOURITES CARD
                    Box(
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(12.dp))
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .clickable { MoveToFavPage(context) }
                            .fillMaxWidth() // Fills the 75% width of the parent Column
                            .height(450.dp) // Adjusted height to fit better with the image
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
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )
                                }
                            }
                            Box(modifier = Modifier.padding(top = 16.dp)) {
                                FavItemList(viewModel2)
                            }
                        }
                    }
                }
            }

            // 前景 UI
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

                    Row {
                        if (!searchActive && !filterActive && !onClickResult)
                        TextButton(onClick = {
                            // 切換語系狀態
                            val targetIsZh = !SchoolDataHolder.isZh
                            SchoolDataHolder.isZh = targetIsZh
                            currentLocale = if (targetIsZh) Locale("zh", "HK") else Locale.ENGLISH
                            Log.d(TAG, "切換至: ${currentLocale.language}")
                        }) {
                            Text(text = stringResource(id = R.string.language))
                        }
                        // 功能按鈕區域
                        Row(
                            modifier = Modifier
                                .fillMaxWidth() // 佔滿寬度，以便將內容推向右側
                                .padding(top = 8.dp, end = 8.dp), // 給頂部和右側留一點邊距
                            horizontalArrangement = Arrangement.End, // 關鍵：將內容推向右端
                            verticalAlignment = Alignment.CenterVertically

                        ){
                            if(!filterActive)
                                IconButton(
                                    onClick = {
                                        val intent = Intent(context, MainActivity::class.java)
//                    context.startActivity(intent)
                                        (context as? Activity)?.finish()
                                        context.startActivity(intent)

                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search, // 或者用 Icons.Default.Tune
                                        contentDescription = stringResource(id = R.string.search),
                                        tint = MaterialTheme.colorScheme.primary
                                    )}
                            if(!filterActive)
                                IconButton(
                                    onClick = {
                                        keyboardController?.hide()
                                        filterActive=true },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FilterList, // 或者用 Icons.Default.Tune
                                        contentDescription =
                                            // if (isZh) "篩選" else "Filter"
                                            stringResource(id = R.string.filter),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }}
                    }


                Spacer(modifier = Modifier.weight(1f))



                if (filterActive) {
                    val viewModel3: FilterViewModel = viewModel()
                    // 注意：這裡直接傳入 SchoolDataHolder.isZh 確保跟隨切換
                    val filterCheckedSet = rememberFilterCheckedSet(viewModel3, SchoolDataHolder.isZh)

                    FilterPanelApp(
                        filterCheckedSet = filterCheckedSet,
                        selectSet = currencySelectedFilters,
                        onBack = { filterActive = false },
                        onConfirm = { selectedFilters ->
                            viewModel.onUpdateFilter(selectedFilters)
                            SchoolDataHolder.currencySelectedFilters = selectedFilters
                            filterActive = false

                            (context as? Activity)?.finish()
                            context.startActivity(Intent(context, MainActivity::class.java))
                        },
                        clarAll = { currencySelectedFilters = emptyMap() }
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
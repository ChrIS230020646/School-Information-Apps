package com.example.comp_3132sef

import android.content.Context
import android.content.Intent
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp_3132sef.ui.school.SchoolViewModel
import com.example.comp_3132sef.ui.school.SearchSchoolViewModel
import java.util.Locale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp_3132sef.data.local.SchoolDataHolder
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.ui.detail.FavItemList
import com.example.comp_3132sef.ui.school.FilterViewModel
import com.example.comp_3132sef.ui.search.FilterPanelApp
import com.example.comp_3132sef.ui.search.rememberFilterCheckedSet
import com.example.comp_3132sef.ui.search.searchBarCompose

class MainActivity1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val schoolVm: SchoolViewModel = viewModel()
            val searchVm: SearchSchoolViewModel = viewModel()
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
fun SearchBarScreen(viewModel: SearchSchoolViewModel , viewModel2: SchoolViewModel) {
//
    val TAG = "DebugMove" // 定義 Log 標籤
    var isZh by remember {mutableStateOf(Locale.getDefault().language == "zh")}
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
//                    SchoolDataHolder.isZh=isZh

                    val intent = Intent(context, SearchResultActivity::class.java)
//                    context.startActivity(intent)

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

    Row(
        verticalAlignment = BiasAlignment.Vertical(0.1f),
        horizontalArrangement = Arrangement.Center, // 讓 Box 在 Row 裡面水平置中
        modifier = Modifier
//            .height((16 * 20).dp)
                .fillMaxHeight()
            .fillMaxWidth()

    ) {

        Box(

            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                    MoveToFavPage(context)
                }
                .fillMaxWidth(0.75f) // 佔據 Row 的 80% 寬度
                .height((16 * 34).dp)
                .padding(16.dp),

//            contentAlignment = Alignment.Center // 讓 Text 在 Box 內部置中
        ) {


            Box(modifier = Modifier
//                .height((16 * 25).dp)
                .padding(top = 32.dp)
                .padding(16.dp),
                contentAlignment = Alignment.Center,


            ){
                FavItemList(viewModel2  )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth() // 佔據父容器寬度的 80%
                    .height(320.dp)    // (16 * 20).dp = 320.dp
//                    .padding(16.dp),
                // 修正點：將 horizontalArrangement 改為 contentAlignment
                //,contentAlignment = Alignment.TopEnd // 將 IconButton 放在 Box 的右上角
            )
            {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter), // Align to top of the card
                horizontalArrangement = Arrangement.SpaceBetween, // Pushes elements to opposite sides
                verticalAlignment = Alignment.CenterVertically // Aligns them vertically
            ) {

                Text(text = "Favourites")
                IconButton(
                    onClick = {
                        /* 點擊邏輯 */
                        MoveToFavPage(context)
                    }
                ) {
                    Icon(
                        imageVector = if (true)
                            Icons.Filled.Star
                        else
                            Icons.Outlined.StarBorder,
                        contentDescription = "收藏",
                        tint = Color.Gray
                    )
                }
            }
            }
        }
    }

    Column(){
        if(!searchActive and !filterActive and !onClickResult) {
            Row() {
                TextButton(onClick = { isZh = !isZh
                    SchoolDataHolder.isZh= isZh
                     }) {
                    Text(if (isZh) "EN" else "中")
                }

            }
        }
//       var searchbarStartPositon= if (!searchActive) 24.dp else 0.dp
//Box(Modifier.padding( start =searchbarStartPositon,)){
//    if(!filterActive)


        val viewModel3: FilterViewModel = viewModel()
        var currencySelectedFilters by remember { mutableStateOf<Map<String, List<String>>>(emptyMap()) }
        val filterCheckedSet = rememberFilterCheckedSet(viewModel3, isZh)
        val keyboardController = LocalSoftwareKeyboardController.current
        Row(
            modifier = Modifier
                .align(Alignment.End)

        ){
            IconButton(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
//                    context.startActivity(intent)

                    context.startActivity(intent)
                     },
            ) {
                Icon(
                    imageVector = Icons.Default.Search, // 或者用 Icons.Default.Tune
                    contentDescription = if (isZh) "搜尋" else "Search",
                    tint = MaterialTheme.colorScheme.primary
                )}
        IconButton(
            onClick = {
                keyboardController?.hide()
                filterActive=true },
        ) {
            Icon(
                imageVector = Icons.Default.FilterList, // 或者用 Icons.Default.Tune
                contentDescription = if (isZh) "篩選" else "Filter",
                tint = MaterialTheme.colorScheme.primary
            )
        }}
        if(filterActive)
        FilterPanelApp(
//            viewModel = viewModel3,
                isZh = isZh,
                filterCheckedSet=filterCheckedSet,
                selectSet=currencySelectedFilters,
                onBack = {

                    filterActive=false
                },
                onConfirm = { selectedFilters ->
                    // 1. 執行你的搜尋邏輯
                    println("User selected: $selectedFilters")

                    viewModel.onUpdateFilter(selectedFilters)
                    filterActive=false
                    SchoolDataHolder.currencySelectedFilters=selectedFilters
//                viewModel.onSearchQueryChange(SchoolDataHolder.query)
                    viewModel.onUpdateFilter(SchoolDataHolder.currencySelectedFilters)

                    SchoolDataHolder.isZh=isZh
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)

                },clarAll={
                    currencySelectedFilters = emptyMap()
                }

            )


    }}

//}



fun MoveToFavPage(context: Context) {
//    val context = LocalContext.current
    val intent = Intent(context, FavActivity::class.java)
    context.startActivity(intent)
}







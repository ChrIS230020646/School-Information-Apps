package com.example.comp_3132sef

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.ui.school.SchoolViewModel
import com.example.comp_3132sef.ui.school.SearchSchoolViewModel
import java.util.Locale
import androidx.compose.ui.graphics.Color
import com.example.comp_3132sef.ui.detail.FavItemList
import com.example.comp_3132sef.ui.detail.searchBarCompose

class MainActivity1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchBarScreen()
                }
            }
        }


    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarScreen(viewModel: SearchSchoolViewModel = viewModel(), viewModel2: SchoolViewModel = viewModel()) {
//
    val isZh = Locale.getDefault().language == "zh"


    Row(
        verticalAlignment = BiasAlignment.Vertical(0.1f),
        horizontalArrangement = Arrangement.Center, // 讓 Box 在 Row 裡面水平置中
        modifier = Modifier
            .height((16 * 200).dp)
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
                FavItemList(viewModel2 , isZh )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth() // 佔據父容器寬度的 80%
                    .height(320.dp)    // (16 * 20).dp = 320.dp
//                    .padding(16.dp),
                // 修正點：將 horizontalArrangement 改為 contentAlignment
                ,contentAlignment = Alignment.TopEnd // 將 IconButton 放在 Box 的右上角
            )
            {
                IconButton(
                    onClick = {
                        /* 點擊邏輯 */
                        MoveToFavPage()
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
    searchBarCompose(viewModel, viewModel2,isZh)

}


fun MoveToFavPage() {
    TODO("Not yet implemented")
}

//@Composable
fun mappingToChooseResult(selectedSchool: SchoolEntity?,viewModel2: SchoolViewModel,school: SchoolEntity){

    if (selectedSchool != null) {
        viewModel2.closeSchoolMap()}
    viewModel2.openSchoolMap(school)
}



/**
LazyListScope 的擴充函式 (Extension Function)，
 */
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





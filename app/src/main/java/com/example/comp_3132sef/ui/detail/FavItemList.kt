package com.example.comp_3132sef.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp_3132sef.data.local.SchoolDataHolder
import com.example.comp_3132sef.ui.school.SchoolViewModel

@Composable
fun FavItemList(viewModel: SchoolViewModel) {
    // 1. 觀察 ViewModel 中的 StateFlow
    // 加上 initial = emptyList() 確保初始狀態安全
//    val favorites by viewModel.favorites.collectAsState()
    val favoritesInfo by viewModel.favoriteSchoolEntities.collectAsState()
    // 2. 使用 LazyColumn 建立列表
    var isZh=SchoolDataHolder.isZh
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            4.dp
            ,top = 0.dp
            //16.dpf

        ), // 列表四周留白

        verticalArrangement = Arrangement.spacedBy(16.dp) // 項目之間的間距
    ) {
        // 3. 渲染列表項目
        items(favoritesInfo.toList()) {
                favorite ->
            val displayName =
                if (SchoolDataHolder.isZh) (favorite.chineseName ?: favorite.englishName)
                else favorite.englishName
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
//                    .aspectRatio(1f)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = Color(0xFFEEEEEE),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp, top = 8.dp, end = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 32.dp)
                    ,
//                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 顯示 Set 中的字串內容
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 12.sp
                        ,lineHeight = 14.sp,

                        fontWeight = FontWeight.Bold,     // 設定為粗體
//                        style = MaterialTheme.typography.titleLarge // 或者使用內建的主題樣式
                    )



                }
                // 右側的星星按鈕
                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                    ,
                    onClick = {
                        // 呼叫刪除邏輯，例如：viewModel.toggleFavorite(schoolName)
                        viewModel.toggleFavorite(favorite.englishName)

                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "移除收藏",
                        tint = Color(0xFFFFD700) // 金色
                    )
                }
            }


        }


    }
}
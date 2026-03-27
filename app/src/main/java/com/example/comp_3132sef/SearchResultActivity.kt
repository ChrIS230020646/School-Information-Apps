package com.example.comp_3132sef

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.example.comp_3132sef.data.local.SchoolDataHolder
import com.example.comp_3132sef.data.local.SchoolDataHolder.isZh
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.ui.detail.SchoolDetailScreen

class SearchResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val school = SchoolDataHolder.selectedSchool
        // 2. 顯示 UI (Display UI)
        setContent {
//            Text(if (isZh) "EN" else "中")
            if (school != null) {
                // 這裡可以呼叫你的 SchoolDetailScreen
                // Call your SchoolDetailScreen here

                SchoolDetailScreen(school = school, onBack = { finish() })
            } else {
                // 處理資料遺失的情況 (Handle missing data)
                finish()
            }
        }
    }
}
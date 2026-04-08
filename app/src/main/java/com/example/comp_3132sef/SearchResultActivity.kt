package com.example.comp_3132sef

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp_3132sef.data.local.SchoolDataHolder
import com.example.comp_3132sef.ui.detail.SchoolDetailScreen
import com.example.comp_3132sef.ui.school.SchoolViewModel

class SearchResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val school = SchoolDataHolder.selectedSchool

        setContent {
            val viewModel: SchoolViewModel = viewModel()

            if (school != null) {
                // Call your SchoolDetailScreen here

                SchoolDetailScreen(school = school, onBack = { finish() }
                )
            } else {

                finish()
            }
        }
    }
}
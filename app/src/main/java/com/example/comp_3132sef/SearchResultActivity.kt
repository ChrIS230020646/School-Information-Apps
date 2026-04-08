package com.example.comp_3132sef

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp_3132sef.data.local.SchoolDataHolder
import com.example.comp_3132sef.ui.detail.SchoolDetailScreen
import com.example.comp_3132sef.ui.school.SchoolViewModel
import com.example.comp_3132sef.ui.theme.COMP_3132SEFTheme

class SearchResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val school = SchoolDataHolder.selectedSchool

        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                COMP_3132SEFTheme {
            val viewModel: SchoolViewModel = viewModel()

            if (school != null) {
                // Call your SchoolDetailScreen here

                SchoolDetailScreen(school = school, onBack = { finish() }
                )
            } else {

                finish()
            }
        }}}
    }
}
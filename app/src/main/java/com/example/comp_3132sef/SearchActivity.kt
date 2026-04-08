package com.example.comp_3132sef

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp_3132sef.ui.detail.SchoolSearchScreen
import com.example.comp_3132sef.ui.school.SchoolViewModel
import com.example.comp_3132sef.ui.school.SearchSchoolViewModel
import com.example.comp_3132sef.ui.theme.COMP_3132SEFTheme

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Note: You should ideally initialize your Room DB in a Singleton
        // or Application class, not directly in the Activity.

        setContent {
            // Use your app's custom theme
//            COMP_3132SEFTheme {
                // A surface container using the 'background' color from the theme
            val schoolVm: SchoolViewModel = viewModel()
            COMP_3132SEFTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    HomeMainScreen()
                }
            }
        }
    }
}

@Composable
fun HomeMainScreen(
    // The viewModel() function automatically handles the lifecycle
    viewModel: SearchSchoolViewModel = viewModel()
) {
    // Just call your Search Screen directly here
    SchoolSearchScreen(viewModel)
}
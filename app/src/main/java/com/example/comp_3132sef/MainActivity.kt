package com.example.comp_3132sef

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
import com.example.comp_3132sef.ui.school.SchoolViewModel
import com.example.comp_3132sef.ui.theme.COMP_3132SEFTheme
import com.example.comp_3132sef.ui.detail.SchoolDetailScreen
import java.util.Locale

import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()


        setContent {
            COMP_3132SEFTheme {
                MainScreen()
            }
        }

    }
}

@Composable
fun MainScreen(
    viewModel: SchoolViewModel = viewModel()
) {
    val selectedSchool by viewModel.selectedSchool.collectAsState()

    if (selectedSchool != null) {
        Column {
            SchoolDetailScreen(
                school = selectedSchool!!,
                onBack = { viewModel.closeSchoolMap() }
            )
        }
    } else {
        SchoolListScreen(viewModel)
    }
}

@Composable
fun SchoolListScreen(
    viewModel: SchoolViewModel
) {
    val favorites by viewModel.favorites.collectAsState()
    val schools by viewModel.schoolEntities.collectAsState()

    val isZh = Locale.getDefault().language == "zh"

    val context = LocalContext.current





    LazyColumn {
        items(schools) { school ->

            val displayName =
                if (isZh) (school.chineseName ?: school.englishName)
                else school.englishName

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openSchoolMap(school) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayName,
                    modifier = Modifier.weight(1f)
                )

                val favKey = school.englishName

                IconButton(onClick = { viewModel.toggleFavorite(favKey) }) {
                    Icon(
                        imageVector =
                        if (favorites.contains(favKey))
                            Icons.Filled.Star
                        else
                            Icons.Outlined.StarBorder,
                        contentDescription = null,
                        tint =
                        if (favorites.contains(favKey))
                            Color(0xFFFFC107)
                        else
                            Color.Gray
                    )
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Your main screen content (List, Column, etc.)

        // 2. The FAB placed manually
        FloatingActionButton(
            onClick = {
                try {
                    // 1. Create the Intent
                    val intent = Intent(context, SearchActivity::class.java).apply {
                        putExtra("EXTRA_DATA", "Hello from Compose!")
                    }

                    // 2. Try to start the Activity
                    context.startActivity(intent)

                } catch (e: Exception) {
                    // 3. Handle the "problem"
                    // This logs it to your Logcat for debugging
                    android.util.Log.e("NavigationError", "Failed to launch HomeActivity", e)

                    // This shows a popup to the user so they know something went wrong
                    android.widget.Toast.makeText(
                        context,
                        "Error: ${e.localizedMessage}",
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                }

            },
            modifier = Modifier
                .align(Alignment.TopEnd) // Put it in the top right!
                .padding(top = 40.dp, end = 16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
        }
    }


//    @Composable
//    fun SchoolListScreen(navController: NavController) {
//        Scaffold(
//            // 1. 定義懸浮按鈕
//            floatingActionButton = {
//                FloatingActionButton(
//                    onClick = {
//                        // 2. 點擊後的導航邏輯，跳轉到 "add_school" 頁面
//                        navController.navigate("add_school")
//                    },
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    contentColor = MaterialTheme.colorScheme.onPrimary
//                ) {
//                    Icon(Icons.Filled.Add, contentDescription = "新增學校")
//                }
//            },
//            // 按鈕的位置（可選，預設在右下角）
//            floatingActionButtonPosition = FabPosition.End,
//            content = TODO()
//        )
//    }
}
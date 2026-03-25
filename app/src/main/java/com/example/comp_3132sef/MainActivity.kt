package com.example.comp_3132sef

import android.app.Activity
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp_3132sef.ui.school.SchoolViewModel
import com.example.comp_3132sef.ui.theme.COMP_3132SEFTheme
import com.example.comp_3132sef.ui.detail.SchoolDetailScreen
import java.util.Locale

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.platform.LocalContext
import com.example.comp_3132sef.data.local.SchoolDataHolder
import com.example.comp_3132sef.ui.school.SearchSchoolViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val TAG = "DebugMove" // 定義 Log 標籤

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: SchoolViewModel = viewModel()
            COMP_3132SEFTheme {

                // 使用更安全的判斷方式 (Use safer null checks)
                val queryText = SchoolDataHolder.query?: ""
                val filters = SchoolDataHolder.currencySelectedFilters ?: emptyMap()

                val hasFilter = filters.isNotEmpty()
                val hasQuery = queryText.isNotBlank()

                Surface(modifier = Modifier.fillMaxSize()) {
                    if (hasFilter || hasQuery) {
                        Log.d("DebugMove", "Mode: Search Result (Q: '$queryText', F: ${filters.size})")
                        searchResultScreen(viewModel)
                    } else {
                        Log.d("DebugMove", "Mode: Main List (Displaying all items)")
                        MainScreen(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun searchResultScreen(
    viewModel: SchoolViewModel ,
    searchSchoolViewModel:SearchSchoolViewModel= viewModel(),
//    back: () -> Unit
){
    val TAG = "DebugMove"
    val isZh = Locale.getDefault().language == "zh"
    val context = LocalContext.current

    // 使用 LaunchedEffect 確保只在進入此頁面時設定一次 ViewModel 狀態
    LaunchedEffect(SchoolDataHolder.query, SchoolDataHolder.currencySelectedFilters) {
        Log.d(TAG, "Initializing Search Results with Holder Data")
        searchSchoolViewModel.onSearchQueryChange(SchoolDataHolder.query.toString())
        searchSchoolViewModel.onUpdateFilter(SchoolDataHolder.currencySelectedFilters)
    }

    // 觀察結果
    val searchResults by searchSchoolViewModel.searchResults.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    LazyColumn {
        items(searchResults) { school ->
            Log.d(TAG, "school.chineseName {$school.chineseName} ")
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
//                    // 1. Create the Intent
//                    val intent = Intent(context, SearchActivity::class.java).apply {
//                        putExtra("EXTRA_DATA", "Hello from Compose!")
//                    }
                    (context as? Activity)?.finish()
                    // 2. Try to start the Activity
//                    context.startActivity(intent)

                } catch (e: Exception) {
                    // 3. Handle the "problem"
                    // This logs it to your Logcat for debugging
                    Log.e("NavigationError", "Failed to launch HomeActivity", e)

                    // This shows a popup to the user so they know something went wrong
                    Toast.makeText(
                        context,
                        "Error: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            },
            modifier = Modifier
                .align(Alignment.TopEnd) // Put it in the top right!
                .padding(top = 40.dp, end = 16.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "返回")
        }
    }
}

@Composable
fun MainScreen(
    viewModel: SchoolViewModel,

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
//                    // 1. Create the Intent
//                    val intent = Intent(context, SearchActivity::class.java).apply {
//                        putExtra("EXTRA_DATA", "Hello from Compose!")
//                    }
                    (context as? Activity)?.finish()
                    // 2. Try to start the Activity
//                    context.startActivity(intent)

                } catch (e: Exception) {
                    // 3. Handle the "problem"
                    // This logs it to your Logcat for debugging
                    Log.e("NavigationError", "Failed to launch HomeActivity", e)

                    // This shows a popup to the user so they know something went wrong
                    Toast.makeText(
                        context,
                        "Error: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            },
            modifier = Modifier
                .align(Alignment.TopEnd) // Put it in the top right!
                .padding(top = 40.dp, end = 16.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "返回")
        }
    }



}

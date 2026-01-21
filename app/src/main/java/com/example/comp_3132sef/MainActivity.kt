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

    val isZh = java.util.Locale.getDefault().language == "zh"

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
}
package com.example.comp_3132sef

import com.example.comp_3132sef.ui.map.MapScreen
import com.example.comp_3132sef.ui.school.SchoolViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp_3132sef.ui.theme.COMP_3132SEFTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.collectAsState
import com.mapbox.mapboxsdk.Mapbox

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this)
        enableEdgeToEdge()
        setContent {
            COMP_3132SEFTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    COMP_3132SEFTheme {
        Greeting("Android")
    }
}


@Composable
fun MainScreen(
    viewModel: SchoolViewModel = viewModel()
) {
    var showMap by remember { mutableStateOf(false) }

    Column {
        Button(
            onClick = { showMap = !showMap },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showMap) "List" else "Map")
        }

        if (showMap) {
            MapScreen(
                schools = viewModel.schoolsFromDb.collectAsState().value            )
        } else {
            SchoolListScreen(viewModel)
        }
    }
}

@Composable
fun SchoolListScreen(
    viewModel: SchoolViewModel
) {
    val schools = viewModel.schools.collectAsState().value
    val favorites = viewModel.favorites.collectAsState().value

    LazyColumn {
        items(schools) { school ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = school,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { viewModel.toggleFavorite(school) }) {
                    Icon(
                        imageVector =
                        if (favorites.contains(school))
                            Icons.Filled.Star
                        else
                            Icons.Outlined.StarBorder,
                        contentDescription = null,
                        tint =
                        if (favorites.contains(school))
                            Color(0xFFFFC107)
                        else
                            Color.Gray
                    )
                }
            }
        }
    }
}
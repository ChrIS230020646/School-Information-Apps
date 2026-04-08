package com.example.comp_3132sef.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
    import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp_3132sef.data.local.SchoolEntity
import java.net.URLEncoder
import com.google.android.gms.location.LocationServices

import android.util.Log
import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp_3132sef.R
import com.example.comp_3132sef.data.local.SchoolDataHolder
import com.example.comp_3132sef.ui.school.SchoolViewModel
import java.util.Locale

@Composable
fun SchoolDetailScreen(
    school: SchoolEntity,
    onBack: () -> Unit,
    viewModel: SchoolViewModel= viewModel()
) {
    val context = LocalContext.current
    var isZh=SchoolDataHolder.isZh
    //make the data respond to language
    val SchoolName =
        if (isZh) (school.chineseName ?: school.englishName)
        else school.englishName
    val SchoolName2 =
        if (isZh) (school.englishName ?: school.chineseName)
        else school.chineseName
    val address =
        if (isZh) (school.chineseAddress ?: school.englishAddress)
        else school.englishAddress
    val district = if (isZh) (school.chineseDistrict ?: school.district)
    else school.district
    val religion = if (isZh) (school.chineseReligion ?: school.religion)
    else school.religion
    val Category = if (isZh) (school.chineseCategory ?: school.englishCategory)
    else school.englishCategory
    val Session = if (isZh) (school.chineseSession ?: school.session)
    else school.session
    val isDarkTheme = isSystemInDarkTheme()
    val headerBackgroundColor = if (isDarkTheme) Color.Gray else Color.DarkGray
    val headerTextColor = Color.White
    var currentLocale by remember(SchoolDataHolder.isZh) {
        mutableStateOf(if (SchoolDataHolder.isZh) Locale("zh", "HK") else Locale.ENGLISH)
    }
    val configuration = Configuration(LocalConfiguration.current).apply {
        setLocale(currentLocale)
    }
    val localizedContext = context.createConfigurationContext(configuration)

    CompositionLocalProvider(
        LocalConfiguration provides configuration,
        LocalContext provides localizedContext
    ) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerBackgroundColor)
                .padding(horizontal = 8.dp, vertical = 4.dp), // Adjusted padding
            verticalAlignment = Alignment.CenterVertically // Aligns icon and text vertically
        ) {
            // 1. The Back Button
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = Color.White
                )
            }

            // 2. 標題 (使用 weight 佔滿中間空間，將 Fav Button 推向右邊)
            Text(
                text = SchoolName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = headerTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            val favKey = school.englishName ?: ""
            val favorites by viewModel.favorites.collectAsState()

            IconButton(
                onClick = { viewModel.toggleFavorite(favKey) }
            ) {
                Icon(
                    imageVector = if (favorites.contains(favKey)) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = "Favorite",
                    tint = if (favorites.contains(favKey)) Color(0xFFFFC107) else Color.White
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) { Text(
            text = SchoolName,
            fontSize = 28.sp, // Large font size
            lineHeight = 34.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (isDarkTheme) Color.White else Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp) // Space between name and the box
        )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(3.dp, Color.LightGray),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 10.dp,
                            vertical = 16.dp
                        ) // <--- THIS CONTROLS THE WIDTH
                ) {
                    if (address != null) {
                        DetailRow(stringResource(R.string.Address), address)
                    }
                    DetailRow(stringResource(R.string.Phone_No), school.telephone!!)
                    DetailRow(stringResource(R.string.Session), Session!!)

                    if (district != null) {
                        DetailRow(stringResource(R.string.District), district)
                    }

                    if (religion != null) {
                        DetailRow(stringResource(R.string.Religion), religion)
                    }

                    DetailRow(stringResource(R.string.Category), "${Category} (${Session})")

                    Text(text = " Lat: ${school.latitude}")
                    Text(text = " Lng: ${school.longitude}")
                }
            }

        Spacer(modifier = Modifier.height(16.dp))
        if (school.website != null) {
            Button(
                onClick = {
                    val uri = Uri.parse(school.website)
                    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text(stringResource(R.string.school_website)) // Add a string resource for the button label
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
        Button(
            onClick = {
                val label = URLEncoder.encode(school.englishName, "UTF-8")
                val uri = Uri.parse(
                    "geo:${school.latitude},${school.longitude}?q=${school.latitude},${school.longitude}($label)"
                )
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text(stringResource(R.string.open_in_maps))

        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val client = LocationServices.getFusedLocationProviderClient(context)

                val hasFine = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                val hasCoarse = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (!hasFine && !hasCoarse) {
                    val uri = Uri.parse(
                        "https://www.google.com/maps/dir/?api=1" +
                                "&destination=${school.latitude},${school.longitude}" +
                                "&travelmode=driving"
                    )
                    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    return@Button
                }

                val cts = CancellationTokenSource()
                client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cts.token)
                    .addOnSuccessListener { loc ->
                        Log.d("NAV", "currentLocation=$loc")

                        val uri = if (loc != null) {
                            Uri.parse(
                                "https://www.google.com/maps/dir/?api=1" +
                                        "&origin=${loc.latitude},${loc.longitude}" +
                                        "&destination=${school.latitude},${school.longitude}" +
                                        "&travelmode=driving"
                            )
                        } else {
                            Uri.parse(
                                "https://www.google.com/maps/dir/?api=1" +
                                        "&destination=${school.latitude},${school.longitude}" +
                                        "&travelmode=driving"
                            )
                        }

                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                    .addOnFailureListener { e ->
                        Log.e("NAV", "getCurrentLocation failed", e)
                        val uri = Uri.parse(
                            "https://www.google.com/maps/dir/?api=1" +
                                    "&destination=${school.latitude},${school.longitude}" +
                                    "&travelmode=driving"
                        )
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            //   & "C:\Users\**users_name**\AppData\Local\Android\Sdk\platform-tools\adb.exe" emu geo fix 114.1694 22.3193
            Text(stringResource(R.string.navigate))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCD5C5C))
        ) {
            Text(stringResource(R.string.back))
        }
    }
}
    }
}


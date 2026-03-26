package com.example.comp_3132sef.ui.detail

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material3.HorizontalDivider
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

import androidx.compose.ui.res.stringResource
import com.example.comp_3132sef.R
import com.example.comp_3132sef.data.local.SchoolDataHolder.isZh

@Composable
fun SchoolDetailScreen(
    school: SchoolEntity,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    //make the data respond to language
    val SchoolName =
        if (isZh) (school.chineseName ?: school.englishName)
        else school.englishName
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
    Column(modifier = Modifier.padding(16.dp)) {

        Text(text = SchoolName)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors(containerColor = Color.White)
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
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (school.website != null) {
            Button(
                onClick = {
                    val uri = Uri.parse(school.website)
                    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                },
                modifier = Modifier.fillMaxWidth()
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
            modifier = Modifier.fillMaxWidth()
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
            modifier = Modifier.fillMaxWidth()
        ) {
            //   & "C:\Users\**users_name**\AppData\Local\Android\Sdk\platform-tools\adb.exe" emu geo fix 114.1694 22.3193
            Text(stringResource(R.string.navigate))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.back))
        }
    }
}


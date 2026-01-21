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
import com.example.comp_3132sef.data.local.SchoolEntity
import java.net.URLEncoder
import com.google.android.gms.location.LocationServices

import android.util.Log
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

import androidx.compose.ui.res.stringResource
import com.example.comp_3132sef.R

@Composable
fun SchoolDetailScreen(
    school: SchoolEntity,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = school.englishName)

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Lat: ${school.latitude}")
        Text(text = "Lng: ${school.longitude}")

        Spacer(modifier = Modifier.height(16.dp))

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


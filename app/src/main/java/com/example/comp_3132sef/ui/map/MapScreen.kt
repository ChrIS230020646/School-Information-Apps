package com.example.comp_3132sef.ui.map

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.example.comp_3132sef.data.local.SchoolEntity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition

import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory

//import org.maplibre.gl.camera.CameraUpdateFactory

@Composable
fun MapScreen(
    schools: List<SchoolEntity>
) {
    AndroidView(
        factory = { context ->

            val locationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)

            MapView(context).apply {
                onCreate(Bundle())

                getMapAsync { map ->
                    map.setStyle(
                        Style.Builder()
                            .fromUri("https://demotiles.maplibre.org/style.json")
                    ) {

                        schools.forEach { school ->
                            map.addMarker(
                                MarkerOptions()
                                    .position(
                                        LatLng(
                                            school.latitude,
                                            school.longitude
                                        )
                                    )
                                    .title(school.englishName)
                            )
                        }

                        try {
                            locationClient.lastLocation.addOnSuccessListener { location ->
                                if (location != null) {
                                    val userLatLng = LatLng(
                                        location.latitude,
                                        location.longitude
                                    )

                                    map.animateCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            userLatLng,
                                            13.0
                                        )
                                    )

                                    map.addMarker(
                                        MarkerOptions()
                                            .position(userLatLng)
                                            .title("You are here")
                                    )
                                }
                            }
                        } catch (e: SecurityException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        },
        update = { mapView ->
            mapView.onStart()
        }
    )
}
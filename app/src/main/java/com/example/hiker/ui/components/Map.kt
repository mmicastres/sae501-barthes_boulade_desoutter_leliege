package com.example.hiker.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun Map(latitude: Double, longitude: Double) {
    val context = LocalContext.current

    // Create the map view
    val mapView = MapView(context)

    // Enable user location
    val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
    myLocationOverlay.enableMyLocation()

    // Manually set the user's location
    myLocationOverlay.enableFollowLocation()

    mapView.overlays.add(myLocationOverlay)

    // Customize other map settings as needed
    mapView.setMultiTouchControls(true)
    

    // Zoom to the user's location
    mapView.controller.setZoom(21.0) // Adjust the zoom level as needed
    mapView.controller.animateTo(latitude.toInt(), longitude.toInt())

    // Compose UI with the MapView
    AndroidView(
        factory = { mapView },
        update = { /* Optional: Handle updates or customization */ },
        modifier = Modifier.fillMaxSize()
    )
}



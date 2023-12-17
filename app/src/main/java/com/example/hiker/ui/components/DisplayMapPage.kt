package com.example.hiker.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import androidx.compose.ui.viewinterop.AndroidView
import com.example.hiker.services.LocationService


@Composable
fun MapPage(locationService: LocationService) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        UniversalButton(
            onClickAction = { locationService.toggleLocationUpdates() },
            buttonText = if (locationService.isLocationAvailable()) "Stop Location Updates" else "Start Location Updates"
        )
    }

    if (locationService.isLocationAvailable()) {
        val context = LocalContext.current

        // Create the map view
        val mapView = MapView(context)

        // Enable user location
        val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
        myLocationOverlay.enableMyLocation()

        // Customize other map settings as needed
        mapView.setMultiTouchControls(true)

        // Manually set the user's location
        myLocationOverlay.enableFollowLocation()

        // Zoom to the user's location
        mapView.controller.setZoom(21.0) // Adjust the zoom level as needed
        mapView.controller.animateTo(locationService.lat!!.toInt(), locationService.lon!!.toInt())

        // Add the overlay to the map
        mapView.overlays.add(myLocationOverlay)

        // Compose UI with the MapView
        AndroidView(
            factory = { mapView },
            update = { /* Optional: Handle updates or customization */ },
            modifier = Modifier.fillMaxSize()
        )
    }


    /*


     */
}

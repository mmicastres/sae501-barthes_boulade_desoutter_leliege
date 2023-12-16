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
import org.osmdroid.util.GeoPoint


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

        var currentCenter: GeoPoint? = null

        // Compose UI with the MapView
        AndroidView(
            factory = { context ->
                // Create the map view
                val mapView = MapView(context)

                // Enable user location
                val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
                myLocationOverlay.enableMyLocation()

                // Manually set the user's location
                myLocationOverlay.enableFollowLocation()

                // Disable map scrolling
                //mapView.isScrollable = false

                // Customize other map settings as needed
                //mapView.setMultiTouchControls(true)


                // Zoom to the user's location
                mapView.controller.setZoom(21.0) // Adjust the zoom level as needed

                // Limiter le scroll
                mapView.setScrollableAreaLimitLatitude(locationService.lat!!, locationService.lat!!, 10)
                mapView.setScrollableAreaLimitLongitude(locationService.lon!!, locationService.lon!!, 10)

                // Add the overlay to the map
                mapView.overlays.add(myLocationOverlay)

                mapView
            },
            update = { mapView ->
                // Mettre à jour le centre de la carte si les coordonnées changent
                val newCenter = GeoPoint(locationService.lat!!, locationService.lon!!)
                if (currentCenter != newCenter) {

                    mapView.resetScrollableAreaLimitLatitude()
                    mapView.resetScrollableAreaLimitLongitude()
                    // Faire défiler (scroll) vers la nouvelle position
                    mapView.controller.animateTo(newCenter)
                    currentCenter = newCenter
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

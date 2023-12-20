package com.example.hiker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.hiker.services.LocationService
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


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
                // Creation de la map
                val mapView = MapView(context)

                // Autoriser la position de l'utilisateur
                val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
                myLocationOverlay.enableMyLocation()

                // Suivre la position de l'utilisateur
                myLocationOverlay.enableFollowLocation()

                // Autoriser le pincement ecran
                //mapView.setMultiTouchControls(true)


                // Zoomer sur l'utilisateur
                mapView.controller.setZoom(21.0)

                // Ajouter icone utilisateur à la map
                mapView.overlays.add(myLocationOverlay)

                mapView
            },
            update = { mapView ->
                // Mettre à jour le centre de la carte si les coordonnées changent
                val newCenter = GeoPoint(locationService.lat!!, locationService.lon!!)
                if (currentCenter != newCenter) {

                    // Définir la zone de scroll
                    mapView.resetScrollableAreaLimitLatitude()
                    mapView.resetScrollableAreaLimitLongitude()
                    mapView.setScrollableAreaLimitLatitude(locationService.lat!!, locationService.lat!!, 10)
                    mapView.setScrollableAreaLimitLongitude(locationService.lon!!, locationService.lon!!, 10)

                    // Faire défiler (scroll) vers la nouvelle position
                    mapView.controller.animateTo(newCenter)
                    currentCenter = newCenter
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

package com.example.hiker.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.currentCameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun Map(latitude: Double, longitude: Double) {
    val context = LocalContext.current

// Suppose you have the user's current coordinates as separate variables
    val userLatitude = 48.8566
    val userLongitude = 2.3522

// Create the map view
    val mapView = MapView(context)
    mapView.setTileSource(TileSourceFactory.MAPNIK)

// Enable user location
    val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
    myLocationOverlay.enableMyLocation()

// Manually set the user's location
    val userLocation = GeoPoint(userLatitude, userLongitude)
    myLocationOverlay.enableFollowLocation()

    mapView.overlays.add(myLocationOverlay)

// Customize other map settings as needed
    mapView.setMultiTouchControls(true)

// Compose UI with the MapView
    AndroidView(
        factory = { mapView },
        update = { /* Optional: Handle updates or customization */ },
        modifier = Modifier.fillMaxSize()
    )
}

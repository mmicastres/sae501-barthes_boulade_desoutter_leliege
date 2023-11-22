package com.example.hiker

import kotlin.math.*

fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6371.0 // Rayon moyen de la Terre en kilomètres

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return R * c
}

fun CalculDistance(): Double {

    val lat1 = 43.6157184
    val lon1 = 2.1976388
    val lat2 = 43.616263
    val lon2 = 2.209621

    return haversine(lat1, lon1, lat2, lon2)
}


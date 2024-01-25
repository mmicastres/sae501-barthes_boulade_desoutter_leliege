package com.example.hiker.managers

class UserLevelManager {
    fun calculateLevel(totalDistance: Float): Int {
        return (totalDistance / 2000).toInt() + 1
    }

    fun getGrade(level: Int): String {
        return when (level) {
            in 1..4 -> "Apprenti randonneur"
            in 5..9 -> "Randonneur occasionnel"
            in 10..14 -> "Randonneur"
            in 15..19 -> "Randonneur expérimenté"
            in 20..24 -> "Maître randonneur"
            in 25..29 -> "Grand maître randonneur"
            in 30..34 -> "Randonneur suprême"
            else -> "Éminent randonneur"
        }
    }
}

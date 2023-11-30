package com.example.hiker


import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import kotlin.random.Random

class WaterNotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    fun showBasicNotification() {
        val notification = NotificationCompat.Builder(context, "notification")
            .setSmallIcon(R.drawable.hiker_logo)
            .setContentTitle("Vous venez de gagner un niveau !")
            .setContentText("Continuez de macrcher pour en gagner plus !")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVisibility(VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }
}
package com.example.hiker


import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.BADGE_ICON_SMALL
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import kotlin.random.Random

class NotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    fun showBasicNotification() {

        val grandeIcone = BitmapFactory.decodeResource(context.resources, R.drawable.icon)
        val notification = NotificationCompat.Builder(context, "notification")
            .setSmallIcon(R.drawable.icon)
            .setContentTitle("Vous venez de gagner un niveau !")
            .setContentText("Continuez de marcher pour en gagner plus !")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setLargeIcon(grandeIcone)
            .setBadgeIconType(BADGE_ICON_SMALL)
            .setVisibility(VISIBILITY_PUBLIC)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )


    }
}
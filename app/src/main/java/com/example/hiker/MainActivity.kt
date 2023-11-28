@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.hiker

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.hiker.ui.theme.HikerTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState



class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Déplacez la création de waterNotificationService en dehors de la lambda setContent
        val waterNotificationService = WaterNotificationService(this)

        setContent {
            HikerTheme{
                val postNotificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

                // Passez waterNotificationService en tant que paramètre à Bouton
                Column {
                    Button(
                        onClick = {
                            waterNotificationService.showBasicNotification()
                        }
                    ) {
                        Text(text = "Afficher une notification de base")
                    }
                }

            }
        }
    }
}

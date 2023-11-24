package com.example.hiker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.hiker.ui.theme.HikerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column() {
                    var distance = CalculDistance()
                    Log.d("Distance", "Distance parcourue : $distance km")

                    var nvdistance = 5
                    nvdistance += distance.toInt()
                    Log.d("NouvelleDistance", "Nouvelle distance parcourue : $nvdistance km")



                    Text(text = "Distance parcourue : $distance km")
                    Text(text = "Nouvelle distance parcourue : $nvdistance km")
                }

            }
            }
        }
    }


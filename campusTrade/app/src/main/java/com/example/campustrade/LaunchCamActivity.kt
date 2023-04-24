package com.example.campustrade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class LaunchCameraScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            LaunchCameraScreenV(LaunchCameraViewModel())
        }
    }
}
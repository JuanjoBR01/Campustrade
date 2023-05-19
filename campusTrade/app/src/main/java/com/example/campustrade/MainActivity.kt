package com.example.campustrade

import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.campustrade.login.LoginScreenComposable
import com.example.campustrade.login.LoginViewModel
import com.example.campustrade.objects.TimeOfLaunch
import com.example.campustrade.ui.theme.CampustradeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<LoginViewModel>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val startTime = SystemClock.elapsedRealtime()
        super.onCreate(savedInstanceState)
        setContent {
            CampustradeTheme{
                LoginScreenComposable(viewModel = viewModel)
            }
        }

        val endTime = SystemClock.elapsedRealtime()
        val timeLapse = endTime - startTime

        Log.d("TimeOfStart", "The app took $timeLapse milliseconds launching.")
        TimeOfLaunch.time =  timeLapse
        Log.d("TimeOfStart", "The app took ${TimeOfLaunch.time} milliseconds launching.")
    }
}
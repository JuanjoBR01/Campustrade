package com.example.campustrade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.campustrade.login.LoginScreenComposable
import com.example.campustrade.login.LoginViewModel
import com.example.campustrade.ui.theme.CampustradeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampustradeTheme{
                LoginScreenComposable(viewModel = viewModel)

            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CampustradeTheme {
        LoginScreenComposable(viewModel = LoginViewModel(LoginRepository()))
    }
}*/
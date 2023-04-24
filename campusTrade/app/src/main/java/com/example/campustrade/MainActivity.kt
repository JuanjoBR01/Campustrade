package com.example.campustrade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.campustrade.login.LoginRepository
import com.example.campustrade.login.LoginScreenComposable
import com.example.campustrade.login.LoginViewModel
import com.example.campustrade.ui.theme.CampustradeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampustradeTheme{
                LoginScreenComposable(viewModel = LoginViewModel(LoginRepository()))

            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CampustradeTheme {
        LoginScreenComposable(viewModel = LoginViewModel(LoginRepository()))
    }
}
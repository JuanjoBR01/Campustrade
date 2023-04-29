package com.example.campustrade.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.campustrade.ui.theme.CampustradeTheme

class HomeActivityMVVM : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampustradeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column(modifier = Modifier
                        .fillMaxSize())
                    {
                        //readData2(HomeViewModel(HomeRepository()))
                        MyBodyHome2(HomeViewModel(HomeRepository()))
                        MyBottomBar2(HomeViewModel(HomeRepository()))
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    CampustradeTheme {
        Column(modifier = Modifier
            .fillMaxSize())
        {
            //readData2(HomeViewModel(HomeRepository()))
            MyBodyHome2(HomeViewModel(HomeRepository()))
            MyBottomBar2(HomeViewModel(HomeRepository()))
        }
    }
}
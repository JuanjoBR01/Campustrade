package com.example.campustrade.history

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
import com.example.campustrade.home.HomeRepository
import com.example.campustrade.home.HomeViewModel
import com.example.campustrade.home.MyBottomBar2
import com.example.campustrade.ui.theme.CampustradeTheme

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampustradeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column()
                    {
                        MyTopBarTransaction2()
                        MyBodyTransaction2(HistoryViewModel(HistoryRepository()))
                        MyBottomBar2(HomeViewModel(HomeRepository(), HistoryRepository()))
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview4() {
    CampustradeTheme {
        Column()
        {
            MyTopBarTransaction2()
            MyBodyTransaction2(HistoryViewModel(HistoryRepository()))
            MyBottomBar2(HomeViewModel(HomeRepository(),HistoryRepository()))
        }
    }
}
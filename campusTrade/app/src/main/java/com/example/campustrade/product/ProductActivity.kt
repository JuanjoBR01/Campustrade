package com.example.campustrade.product

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.campustrade.history.HistoryRepository
import com.example.campustrade.home.HomeRepository
import com.example.campustrade.home.HomeViewModel
import com.example.campustrade.home.MyBodyHome2
import com.example.campustrade.home.MyBottomBar2
import com.example.campustrade.product.ui.theme.CampustradeTheme

class ProductActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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
                        ProductDetail(ProductViewModel(ProductRepository()))
                        MyBottomBar2(HomeViewModel(HomeRepository(), HistoryRepository()))
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DefaultPreviewProduct() {
    CampustradeTheme {
        Column(modifier = Modifier
            .fillMaxSize())
        {
            //readData2(HomeViewModel(HomeRepository()))
            ProductDetail(ProductViewModel(ProductRepository()))
            MyBottomBar2(HomeViewModel(HomeRepository(), HistoryRepository()))
        }
    }
}
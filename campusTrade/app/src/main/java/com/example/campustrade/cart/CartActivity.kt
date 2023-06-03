package com.example.campustrade.cart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.campustrade.cart.ui.theme.CampustradeTheme
import com.example.campustrade.history.HistoryRepository
import com.example.campustrade.home.HomeRepository
import com.example.campustrade.home.HomeViewModel
import com.example.campustrade.home.MyBottomBar2
import com.example.campustrade.product.ProductDetail
import com.example.campustrade.product.ProductRepository
import com.example.campustrade.product.ProductViewModel

class CartActivity : ComponentActivity() {
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
                        MyCart(CartViewModel())
                        MyBottomBar2(HomeViewModel(HomeRepository(), HistoryRepository()))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CampustradeTheme {
        Column(modifier = Modifier
            .fillMaxSize())
        {
            //readData2(HomeViewModel(HomeRepository()))
            MyCart(CartViewModel())
            MyBottomBar2(HomeViewModel(HomeRepository(), HistoryRepository()))
        }
    }
}
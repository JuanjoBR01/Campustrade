package com.example.campustrade

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campustrade.ui.theme.CampustradeTheme

class TransactionsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            
            CampustradeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                    ){
                        MyTopBarTransaction()
                        MyBodyTransaction()
                        MyBottomBar()
                    }
                }
            }
        }
    }
}

@Composable
fun MyTopBarTransaction() {
    TopAppBar(
        title = { Text(text = "Transactions",
                        fontSize = 30.sp
                        )
                },
        backgroundColor = Color(0xFFFB8500),
        navigationIcon =  {  Icon(Icons.Filled.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(90.dp))
        }
    )
}

@Composable
fun MyBodyTransaction(){
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    Box(modifier = Modifier.height(screenHeight-130.dp))
    {
        LazyColumn() {
            items(prepareTransactionList()) { item ->
                TransactionList(item)
            }
        }
    }
}

@Composable
fun TransactionList(producto: Product) {
    //producto = Product(name = "Pencils", type = "Material", price = 5000, icon = Icons.Filled.Home)
    Column (modifier = Modifier
        .padding(16.dp)
        .border(2.dp, Color.Black)
        .background(Color(0xFFEBEBEB))
    )
    {
        Box(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = producto.icon,
                contentDescription = producto.name,
                modifier = Modifier.size(100.dp)
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEBEBEB))
        ) {
            Text(
                text = producto.name,
                style = MaterialTheme.typography.h6,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEBEBEB))
        ) {
            Text(
                text = producto.price.toString() + "$",
                style = MaterialTheme.typography.h6,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEBEBEB))
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = producto.type,
                style = MaterialTheme.typography.h6,
                fontSize = 17.sp,
                color = Color(0xFF939393)
            )

        }

    }
}



private fun prepareTransactionList(): List<Product> {
    val productList = arrayListOf<Product>()

    // add menu items
    productList.add(Product(name = "Lab Coat", type = "Accesory", price = 30000, icon = Icons.Filled.Home ))
    productList.add(Product(name = "Pencils", type = "Material", price = 5000, icon = Icons.Filled.Person))
    productList.add(Product(name = "Sneakers", type = "Accesory", price = 100000, icon = Icons.Filled.DateRange))
    productList.add(Product(name = "Sneakers", type = "Accesory", price = 100000, icon = Icons.Filled.DateRange))

    return productList
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    CampustradeTheme {
        Column(modifier = Modifier
            .fillMaxSize()
        ) {
            MyTopBarTransaction()
            MyBodyTransaction()
            MyBottomBar()
        }
    }
}
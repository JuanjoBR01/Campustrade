package com.example.campustrade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campustrade.ui.theme.CampustradeTheme
import com.example.campustrade.ui.theme.darkBlue

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
        title = { Text(text = "History",
                        fontSize = 30.sp,
                        style = MaterialTheme.typography.h1,
                        color = darkBlue
                        )
                },
        backgroundColor = Color(0xFFFB8500),
        navigationIcon =  {  Icon(Icons.Filled.DateRange,
                                    contentDescription = null,
                                    tint = darkBlue,
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
        .border(2.dp, Color.Black, shape = RoundedCornerShape(10.dp))
        .shadow(20.dp)
        .background(Color(0xFFEBEBEB))
    )
    {
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEBEBEB))
                .padding(4.dp)
        ) {
            Text(
                text = producto.date,
                style = MaterialTheme.typography.h6,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF939393)
            )
        }
        Box(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .size(175.dp)
                    .clip(RoundedCornerShape(25.dp)),
                painter = painterResource(returnImage(producto.name)),
                contentDescription = "Imagen",
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

private fun returnImage(nombre: String): Int {
    if(nombre == "Lab Coat") return R.drawable.lab_coat
    else if(nombre == "Sneakers") return R.drawable.sneakers
    else if(nombre == "Lab Mask") return R.drawable.lab_mask
    else if(nombre == "Arduino") return R.drawable.arduino
    else if(nombre == "Pencils") return R.drawable.pencils
    else if(nombre == "Screwdriver Set") return R.drawable.screwdriver_set
    return R.drawable.sneakers
}


private fun prepareTransactionList(): List<Product> {
    val productList = arrayListOf<Product>()

    // add menu items
    productList.add(Product(name = "Lab Coat", type = "Accesory", price = 30000, icon = Icons.Filled.Home, date = "24/08/2022" ))
    productList.add(Product(name = "Pencils", type = "Material", price = 5000, icon = Icons.Filled.Person, date = "12/03/2022"))
    productList.add(Product(name = "Sneakers", type = "Accesory", price = 100000, icon = Icons.Filled.DateRange, date = "2/01/2022"))
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
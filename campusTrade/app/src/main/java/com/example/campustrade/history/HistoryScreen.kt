package com.example.campustrade.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campustrade.Product
import com.example.campustrade.history.HistoryViewModel
import com.example.campustrade.ui.theme.darkBlue

@Composable
fun MyTopBarTransaction2() {
    TopAppBar(
        title = { Text(text = "History",
            fontSize = 30.sp,
            style = MaterialTheme.typography.h1,
            color = darkBlue
        )
        },
        backgroundColor = Color(0xFFFB8500),
        navigationIcon =  {  Icon(
            Icons.Filled.DateRange,
            contentDescription = null,
            tint = darkBlue,
            modifier = Modifier.size(90.dp))
        }
    )
}

@Composable
fun MyBodyTransaction2(historyViewModel: HistoryViewModel){
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val historyList = historyViewModel.prepareTransactionList()

    Box(modifier = Modifier.height(screenHeight-130.dp))
    {
        LazyColumn() {
            items(historyList) { item ->
                TransactionList2(item, historyViewModel)
            }
        }
    }
}

@Composable
fun TransactionList2(producto: Product, historyViewModel: HistoryViewModel) {
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
                painter = painterResource(historyViewModel.returnImage(producto.name)),
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


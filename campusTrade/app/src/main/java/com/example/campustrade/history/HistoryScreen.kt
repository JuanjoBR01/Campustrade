package com.example.campustrade.history

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.campustrade.ConnectivityReceiver
import com.example.campustrade.Product
import com.example.campustrade.ProductDB
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

    val contexto = LocalContext.current

    val numPur :String by historyViewModel.numPur.observeAsState(initial = "")
    val totPur :String by historyViewModel.totPur.observeAsState(initial = "")
    val typePur :String by historyViewModel.typePur.observeAsState(initial = "")
    val condPur :String by historyViewModel.condPur.observeAsState(initial = "")

    historyViewModel.purchaseData(contexto)

    ConnectionLost(context = contexto)
    
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth())
    {
        Column(modifier = Modifier
            .padding(16.dp)
            .border(2.dp, Color.Black, shape = RoundedCornerShape(10.dp))
            .shadow(20.dp)
            .background(Color(0xFFC4E5F5))) {
            Row(modifier = Modifier
                .padding(16.dp))
            {
                Text(
                    text = "Num. Purchases: ",
                    style = MaterialTheme.typography.h6,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = numPur,
                    style = MaterialTheme.typography.h6,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(modifier = Modifier
                .padding(16.dp))
            {
                Text(
                    text = "Total Purchased: ",
                    style = MaterialTheme.typography.h6,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = totPur,
                    style = MaterialTheme.typography.h6,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(modifier = Modifier
                .padding(16.dp))
            {
                Text(
                    text = "Most Purchased Type: ",
                    style = MaterialTheme.typography.h6,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = typePur,
                    style = MaterialTheme.typography.h6,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(modifier = Modifier
                .padding(16.dp))
            {
                Text(
                    text = "Most Purchased Condition: ",
                    style = MaterialTheme.typography.h6,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = condPur,
                    style = MaterialTheme.typography.h6,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if(numPur=="NO") {
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .padding(end = 10.dp, top = 5.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "No Internet Connection or Previous Data Found",
                style = MaterialTheme.typography.h6,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFB8500)
            )
        }
    }
    else
    {
            val data :List<ProductDB> by historyViewModel.productList.observeAsState(emptyList())

            historyViewModel.arrangeProductListFirestore()

            Box(modifier = Modifier.height(screenHeight-370.dp))
            {
                LazyColumn() {
                    items(data) { item ->
                        TransactionList2(item, historyViewModel)
                    }
                }
            }
    }


}

@Composable
fun TransactionList2(producto: ProductDB, historyViewModel: HistoryViewModel) {
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
                text = producto.publishDate,
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
                painter = rememberImagePainter(
                    data = producto.image,
                    builder = {
                        crossfade(true)
                    }
                ),
                contentDescription = "Imagen",
                modifier = Modifier
                    .size(175.dp)
                    .clip(RoundedCornerShape(25.dp))
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

@Composable
fun ConnectionLost(context: Context) {
    val connectivityReceiver = remember { ConnectivityReceiver(context = context) }
    connectivityReceiver.register()

    val openDialog = remember{ mutableStateOf(true) }

    if (!connectivityReceiver.isOnline && openDialog.value == true) {
        AlertDialog(
            onDismissRequest = {openDialog.value = false},
            title = { Text("Disconnected!") },
            text = { Text("No Internet Connection Found. Using previous information. Please connect again to update information!") },
            confirmButton = {},
            dismissButton = {}
        )
        Log.d("ConnectionEvent", "Lost connectivity")
    }

    DisposableEffect(key1 = connectivityReceiver) {
        onDispose {
            connectivityReceiver.unregister()
        }
    }
}
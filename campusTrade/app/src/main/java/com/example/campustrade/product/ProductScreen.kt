package com.example.campustrade.product

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.campustrade.ConnectivityReceiver
import com.example.campustrade.ProductDB
import com.example.campustrade.R
import com.example.campustrade.home.ConnectionLost
import com.example.campustrade.home.HomeViewModel
import com.example.campustrade.home.ProductList2
import com.example.campustrade.ui.theme.black

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductDetail(productViewModel: ProductViewModel){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val contexto = LocalContext.current.applicationContext

    val contextCurr = LocalContext.current

    val maxChar = 25

    //val preference :String by homeViewModel.value.observeAsState(initial = "Accesory")

    var preference by remember { mutableStateOf("Accesory") }
    var expanded by remember { mutableStateOf(false) }


    TopAppBar(modifier = Modifier.height(75.dp),
        title = {
                Text(modifier = Modifier.height(60.dp),
                    text = "Product Detail",
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp

                )
        },
        backgroundColor = Color(0xFFD3AEF3),
        navigationIcon = {
            IconButton(onClick = {
                productViewModel.backPage(contextCurr)
            },
                interactionSource = interactionSource) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Navigation icon",
                    modifier = Modifier.size(45.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = { productViewModel.goToCart(contextCurr) }) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "", modifier = Modifier.size(65.dp))
            }
        }
    )

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val producto: ProductDB by productViewModel.productInfo.observeAsState(initial = ProductDB())

    productViewModel.getProductDB(contexto)

    ConnectionLostProduct(context = contexto)

    val connectivityReceiver = remember { ConnectivityReceiver(context = contexto) }
    connectivityReceiver.register()
    var screenH2 = 130.dp
    if(!connectivityReceiver.isOnline){
        screenH2 = 170.dp
    }
    Box(modifier = Modifier.height(screenHeight - screenH2))
    {
        Column (modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFFEBEBEB))
        )
        {
            var discount = " "
            if(producto.type == "Accesory") {
                discount = "-" + productViewModel.getDiscount().toString() + "%"
            }
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .padding(end = 10.dp, top = 5.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFEBEBEB))
            ) {
                Text(
                    text = discount,
                    style = MaterialTheme.typography.h6,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFB8500)
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
                    fontSize = 40.sp,
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
                    text = producto.type,
                    style = MaterialTheme.typography.h6,
                    fontSize = 22.sp,
                    color = Color(0xFF939393)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = producto.image,
                        builder = {
                            crossfade(true)
                            placeholder(R.drawable.product)
                            error(R.drawable.product)
                        }
                    ),
                    contentDescription = "Imagen",
                    modifier = Modifier
                        .size(280.dp)
                        .clip(RoundedCornerShape(25.dp))
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEBEBEB))
                    .padding(horizontal = 34.dp)
            ) {
                Text(
                    text = producto.description,
                    style = MaterialTheme.typography.h4,
                    fontSize = 17.sp
                )

            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEBEBEB))
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Price: " + producto.price.toString() + "$",
                    style = MaterialTheme.typography.h4,
                    fontSize = 23.sp,
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
                Row()
                {
                    Column(){
                    Text(
                        text = "Condition: " + producto.condition + "  -  ",
                        style = MaterialTheme.typography.h6,
                        fontSize = 17.sp,
                        color = Color(0xFF939393)
                    )
                    }
                    Column() {
                        Text(
                            text = "Stock: " + producto.stock.toString(),
                            style = MaterialTheme.typography.h6,
                            fontSize = 17.sp,
                            color = Color(0xFF939393)
                        )
                    }
                }
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEBEBEB))
                    .padding(vertical = 8.dp)
            ) {
                Button(onClick = {
                    if(connectivityReceiver.isOnline) {
                        productViewModel.addToCart(contextCurr, producto)
                    }
                }
                ){
                    Text(text = "Add to cart!")
                }

            }
        }
    }

    DisposableEffect(key1 = connectivityReceiver) {
        onDispose {
            connectivityReceiver.unregister()
        }
    }
}

@Composable
fun ConnectionLostProduct(context: Context) {
    val connectivityReceiver = remember { ConnectivityReceiver(context = context) }
    connectivityReceiver.register()

    val openDialog = remember{ mutableStateOf(true) }

    if (!connectivityReceiver.isOnline && openDialog.value == true) {
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .padding(end = 10.dp, top = 5.dp, start = 15.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "No Internet Connection Found. Please connect again to enable cart features!",
                style = MaterialTheme.typography.h6,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFB8500)
            )
        }
        Log.d("ConnectionEvent", "Lost connectivity")
    }

    DisposableEffect(key1 = connectivityReceiver) {
        onDispose {
            connectivityReceiver.unregister()
        }
    }
}

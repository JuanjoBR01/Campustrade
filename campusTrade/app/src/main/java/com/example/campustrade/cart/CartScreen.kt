package com.example.campustrade.cart

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.campustrade.ConnectivityReceiver
import com.example.campustrade.ProductDB
import com.example.campustrade.home.ProductList2
import com.example.campustrade.product.ProductViewModel

@Composable
fun MyCart(cartViewModel: CartViewModel){
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
                text = "My Cart",
                textAlign = TextAlign.Center,
                fontSize = 30.sp

            )
        },
        backgroundColor = Color(0xFFF5C178),
        navigationIcon = {
            IconButton(onClick = {
                cartViewModel.backPage(contextCurr)
            },
                interactionSource = interactionSource) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Navigation icon",
                    modifier = Modifier.size(45.dp)
                )
            }
        }
    )

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    ConnectionLostCart(context = contexto)

    val connectivityReceiver = remember { ConnectivityReceiver(context = contexto) }
    connectivityReceiver.register()
    var screenH2 = 250.dp
    if(connectivityReceiver.isOnline){
        screenH2 = 300.dp
    }

    val data :List<ProductDB> by cartViewModel.productList.observeAsState(emptyList())

    cartViewModel.getLRU()

    val codigo :String by cartViewModel.codigo.observeAsState("Init")

    Box() {
        Row() {
            Column() {
                Box() {
                    Text(
                        text = "Promotional Code:",
                        style = MaterialTheme.typography.h6,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }
            Column() {
                Box() {
                    Text(
                        text = codigo,
                        style = MaterialTheme.typography.h6,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }
        }
    }

    cartViewModel.checkFile(contexto)

    var texto by remember { mutableStateOf("") }

    Box()
    {
        Row(){
            Column()
            {
                Box(modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)) {
                    TextField(
                        modifier = Modifier
                            .width(250.dp)
                            .height(60.dp)
                            .align(Alignment.Center)
                            .padding(start = 16.dp, bottom = 10.dp),
                        value = texto,
                        singleLine = true,
                        placeholder = {},
                        onValueChange = {nuevoTexto ->
                            if (nuevoTexto.length <= 15)
                            {
                                texto = nuevoTexto
                            }

                        },
                    )
                }
            }
            Column()
            {
                Box(modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)) {
                    Button(onClick =
                    {
                        cartViewModel.saveToFile(contexto, texto)
                    },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFFF8707), // Set background color
                            contentColor = MaterialTheme.colors.onPrimary // Set content color
                        )
                    ){
                        Text(text = "Reedem")
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.height(screenHeight - screenH2))
    {
        LazyColumn(){
            items(data) { item ->
                val cant = cartViewModel.getLRUCant(item)
                if (cant != null) {
                    cartProduct(item, cant, cartViewModel)
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
fun cartProduct(producto: ProductDB, cant: Int, cartViewModel: CartViewModel){
    val context = LocalContext.current
    val contexto = LocalContext.current.applicationContext
    Row (modifier = Modifier
        .padding(16.dp)
        .border(2.dp, Color.Black, shape = RoundedCornerShape(10.dp))
        .shadow(20.dp)
        .fillMaxWidth()
        .background(Color(0xFFEBEBEB))
    )
    {
        Column(){
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .padding(15.dp)
                    .background(Color(0xFFEBEBEB))
            ) {
                Text(
                    text = producto.name + " - " + producto.condition,
                    style = MaterialTheme.typography.h6,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .background(Color(0xFFEBEBEB))
            ) {
                Text(
                    text = producto.price.toString() + "$  -  " + producto.type ,
                    style = MaterialTheme.typography.h6,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5F574D),
                    modifier = Modifier.padding(15.dp)
                )

            }
        }
        Column(){
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .background(Color(0xFFEBEBEB))
            ) {
                Text(
                    text = "Cant: " + cant.toString(),
                    style = MaterialTheme.typography.h6,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEBEBEB))
                    .padding(end = 20.dp)
            ) {
                Button(onClick = {
                    cartViewModel.deleteFromLRU(producto)
                },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFFFC107), // Set background color
                        contentColor = MaterialTheme.colors.onPrimary // Set content color
                    )
                ){
                    Text(text = "Delete")
                }

            }
        }
    }
}

@Composable
fun ConnectionLostCart(context: Context) {
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
        AlertDialog(
            onDismissRequest = {openDialog.value = false},
            title = { Text("Disconnected!") },
            text = { Text("Try to go back online!") },
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

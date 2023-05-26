package com.example.campustrade.home

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.campustrade.ConnectivityReceiver
import com.example.campustrade.ProductDB
import com.example.campustrade.history.HistoryViewModel
import com.example.campustrade.ui.theme.black


@Composable
fun readData2(homeViewModel: HomeViewModel){

    val data :List<ProductDB> by homeViewModel.productList.observeAsState(emptyList())

}

@Composable
fun MyBodyHome2(homeViewModel: HomeViewModel){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val contexto = LocalContext.current.applicationContext

    val maxChar = 25
    val value :String by homeViewModel.value.observeAsState(initial = "")

    //val preference :String by homeViewModel.value.observeAsState(initial = "Accesory")

    var preference by remember { mutableStateOf("Accesory") }
    var expanded by remember { mutableStateOf(false) }

    if (isPressed)
        MyBodyHome2(homeViewModel)

    TopAppBar(modifier = Modifier.height(75.dp),
        title = {
            Column() {
                TextField(modifier = Modifier.height(60.dp),
                    value = value,
                    singleLine = true,
                    placeholder = { Text(text = "Search", fontSize = 22.sp) },
                    onValueChange = {
                        homeViewModel.onSearchChange(it)
                    },
                )
                Text(
                    text = "${value.length} / $maxChar",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    color = black
                )
            }
        },
        backgroundColor = Color(0xFF8ECAE6),
        navigationIcon = {
            IconButton(onClick = {expanded = !expanded},
                interactionSource = interactionSource) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Navigation icon", modifier = Modifier.size(90.dp))
            }
            if (expanded) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color(0xFFB4E7FF))
                ) {
                    DropdownMenuItem(onClick = {
                        // acción para el primer elemento de menú
                        //homeViewModel.onPreferenceChange("Type")
                        preference = "Accesory"
                        homeViewModel.arrangeProductListFirestore(value, preference)
                        homeViewModel.saveToSharedPreferences(contexto, "preference", preference)
                        expanded = false
                    },
                    modifier = Modifier.background(Color(0xFFB4E7FF)))
                    {
                        Text(text = "Type")
                    }
                    DropdownMenuItem(onClick = {
                        // acción para el segundo elemento de menú
                        //homeViewModel.onPreferenceChange("Used")
                        preference = "Used"
                        homeViewModel.arrangeProductListFirestore(value, preference)
                        homeViewModel.saveToSharedPreferences(contexto, "preference", preference)
                        expanded = false
                    },
                    modifier = Modifier.background(Color(0xFFB4E7FF)))
                    {
                        Text(text = "Condition - Used")
                    }
                    DropdownMenuItem(onClick = {
                        // acción para el segundo elemento de menú
                        //homeViewModel.onPreferenceChange("New")
                        preference = "New"
                        homeViewModel.arrangeProductListFirestore(value, preference)
                        homeViewModel.saveToSharedPreferences(contexto, "preference", preference)
                        expanded = false
                    },
                        modifier = Modifier.background(Color(0xFFB4E7FF)))
                    {
                        Text(text = "Condition - New")
                    }
                    // Agregar más elementos de menú si es necesario
                }
            }
        }
    )

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    //val productList = homeViewModel.arrangeProductList(value)

    //ConnectionLost(context = contexto)

        val data: List<ProductDB> by homeViewModel.productList.observeAsState(emptyList())

        val prefAnt = homeViewModel.retrieveFromSharedPreferences(contexto, "preference")
        if (prefAnt == null) {
            homeViewModel.arrangeProductListFirestore(value, preference)
        } else {
            homeViewModel.arrangeProductListFirestore(value, prefAnt)
        }

        if (data.size == 0) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.Blue,
                    strokeWidth = 4.dp,
                )
            }
        } else {
            Box(modifier = Modifier.height(screenHeight - 130.dp))
            {
                LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 150.dp)) {
                    items(data) { item ->
                        ProductList2(item, homeViewModel)
                    }
                }
            }
        }
}

@Composable
fun ProductList2(producto: ProductDB, homeViewModel: HomeViewModel) {
    //producto = Product(name = "Pencils", type = "Material", price = 5000, icon = Icons.Filled.Home)
    Column (modifier = Modifier
        .padding(16.dp)
        .border(2.dp, Color.Black, shape = RoundedCornerShape(10.dp))
        .shadow(20.dp)
        .background(Color(0xFFEBEBEB))
    )
    {
        var discount = " "
        if(producto.type == "Accesory") {
            discount = "-" + homeViewModel.getDiscount().toString() + "%"
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
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFB8500)
            )
        }
        Box(
            modifier = Modifier
                .padding(10.dp)
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
            Column(horizontalAlignment = Alignment.CenterHorizontally)
            {
                Text(
                    text = producto.type + " - " + producto.condition,
                    style = MaterialTheme.typography.h6,
                    fontSize = 17.sp,
                    color = Color(0xFF939393)
                )
                Text(
                    text = "Stock: " + producto.stock.toString(),
                    style = MaterialTheme.typography.h6,
                    fontSize = 17.sp,
                    color = Color(0xFF939393)
                )
            }
        }

    }
}



@Composable
fun MyBottomBar2(homeViewModel: HomeViewModel) {
    // items list
    val bottomMenuItemsList = homeViewModel.prepareBottomMenu()

    val contextForToast = LocalContext.current.applicationContext

    var selectedItem by remember {
        mutableStateOf("Publish")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BottomNavigation(
            modifier = Modifier.align(alignment = Alignment.BottomCenter),
            backgroundColor = Color(0xFF023047),
            contentColor = Color.White
        ) {
            // this is a row scope
            // all items are added horizontally

            bottomMenuItemsList.forEach { menuItem ->
                // adding each item
                val textColor = if (menuItem.label == "Publish") "anaranjado" else "azul"
                val context = LocalContext.current
                BottomNavigationItem(
                    selected = (selectedItem == menuItem.label),
                    onClick = {
                        selectedItem = menuItem.label
                        Toast.makeText(
                            contextForToast,
                            menuItem.label, Toast.LENGTH_SHORT
                        ).show()
                        val name = menuItem.label
                        homeViewModel.changePage(name, context)

                    },
                    icon = {
                        Icon(
                            imageVector = menuItem.icon,
                            contentDescription = menuItem.label
                        )
                    },
                    label = {
                        Text(text = menuItem.label)
                    },
                    enabled = true,
                    modifier = Modifier.background(homeViewModel.returnColor(textColor))
                )
            }
        }
    }
}

@Composable
fun ConnectionLost(context: Context) {
    val connectivityReceiver = remember { ConnectivityReceiver(context = context) }
    connectivityReceiver.register()

    if (!connectivityReceiver.isOnline) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Disconnected!") },
            text = { Text("No Internet Connection Found. Please connect again to use all features!") },
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
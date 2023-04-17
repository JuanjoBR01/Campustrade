package com.example.campustrade

import android.R
import android.R.id
import android.content.Intent
import android.media.Image
import android.media.browse.MediaBrowser.MediaItem
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import com.example.campustrade.ui.theme.CampustradeTheme


class HomeActivity : ComponentActivity() {
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
                        MyBodyHome()
                        MyBottomBar()
                    }
                }
            }
        }
    }
}

@Composable
fun MyBodyHome(){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    var value by remember {
        mutableStateOf("")
    }

    if (isPressed)
        MyBodyHome()

    TopAppBar(
        title = { TextField(value = value,
            singleLine = true,
            placeholder={ Text(text="Search",fontSize = 28.sp) },
            onValueChange = { newText ->
                value = newText
            })
        },
        backgroundColor = Color(0xFF8ECAE6),
        navigationIcon = {
            IconButton(onClick = {},
                interactionSource = interactionSource) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Navigation icon", modifier = Modifier.size(90.dp))
            }
        }
    )

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    Box(modifier = Modifier.height(screenHeight-130.dp))
    {
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 150.dp)) {
            items(arrangeProductList(value)) { item ->
                ProductList(item)
            }
        }
    }
}

@Composable
fun ProductList(producto: Product) {
    //producto = Product(name = "Pencils", type = "Material", price = 5000, icon = Icons.Filled.Home)
    Column (modifier = Modifier
        .padding(16.dp)
        .border(2.dp, Color.Black)
        .background(Color(0xFFEBEBEB))
    )
    {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = producto.icon,
                contentDescription = producto.name,
                modifier = Modifier.size(128.dp)
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



private fun prepareProductList(): List<Product> {
    val productList = arrayListOf<Product>()

    // add menu items
    productList.add(Product(name = "Lab Coat", type = "Accesory", price = 30000, icon = Icons.Filled.Star ))
    productList.add(Product(name = "Arduino", type = "Product", price = 400000, icon = Icons.Filled.Phone))
    productList.add(Product(name = "Pencils", type = "Material", price = 5000, icon = Icons.Filled.Create))
    productList.add(Product(name = "Sneakers", type = "Accesory", price = 100000, icon = Icons.Filled.Star))
    productList.add(Product(name = "Lab Mask", type = "Accesory", price = 500000, icon = Icons.Filled.Star))
    productList.add(Product(name = "Screwdriver Set", type = "Product", price = 20000, icon = Icons.Filled.Phone))

    return productList
}

private fun arrangeProductList(search: String): List<Product>{
    val productList = arrayListOf<Product>()
    var finalList = arrayListOf<Product>()
    val actualList = prepareProductList()

    val preference = "Accesory"

    actualList.forEach { producto ->
        if(producto.type == preference)
        {
            productList.add(producto)
        }
    }
    actualList.forEach { producto ->
        if(producto.type != preference)
        {
            productList.add(producto)
        }
    }

    if(search != "")
    {
        productList.forEach { producto ->
            if(producto.name.contains(search))
            {
                finalList.add(producto)
            }
        }
    }
    else{
        finalList = productList
    }

    return finalList
}

data class Product(val name: String, val type: String, val price: Int,  val icon: ImageVector)


@Composable
fun MyBottomBar() {
    // items list
    val bottomMenuItemsList = prepareBottomMenu()

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
                        if(name == "Home")
                        {
                            val intent = Intent(context, HomeActivity::class.java)
                            context.startActivity(intent)
                        }
                        else if(name == "Publish")
                        {
                            val intent = Intent(context, PublishActivity::class.java)
                            context.startActivity(intent)
                        }
                        else if(name == "Transact")
                        {
                            val intent = Intent(context, TransactionsActivity::class.java)
                            context.startActivity(intent)
                        }


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
                    modifier = Modifier.background(returnColor(textColor))
                )
            }
        }
    }
}

private fun returnColor(color: String): Color {
    if(color == "anaranjado")
        return Color(0xFFFB8500)
    else
        return Color(0xFF023047)
}

private fun prepareBottomMenu(): List<BottomMenuItem> {
    val bottomMenuItemsList = arrayListOf<BottomMenuItem>()

    // add menu items
    bottomMenuItemsList.add(BottomMenuItem(label = "Home", icon = Icons.Filled.Home))
    bottomMenuItemsList.add(BottomMenuItem(label = "Explore", icon = Icons.Filled.List))
    bottomMenuItemsList.add(BottomMenuItem(label = "Publish", icon = Icons.Filled.Add))
    bottomMenuItemsList.add(BottomMenuItem(label = "Transact", icon = Icons.Filled.ShoppingCart))
    bottomMenuItemsList.add(BottomMenuItem(label = "Profile", icon = Icons.Filled.Person))

    return bottomMenuItemsList
}

data class BottomMenuItem(val label: String, val icon: ImageVector)

@Preview(showBackground = true)
@Composable
fun DefaultPreviewHome() {
    CampustradeTheme {
        Column(modifier = Modifier
            .fillMaxSize()
        ){
            MyBodyHome()
            MyBottomBar()
        }
    }
}
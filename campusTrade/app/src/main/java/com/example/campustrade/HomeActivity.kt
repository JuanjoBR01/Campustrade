package com.example.campustrade

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campustrade.ui.theme.CampustradeTheme
import com.example.campustrade.ui.theme.black
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase


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
                        readData()
                        MyBodyHome()
                        MyBottomBar()
                    }
                }
            }
        }
    }
}

@Composable
fun readData(){
    // on below line creating an instance of firebase firestore.
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Read the data from Firestore
    var data by remember { mutableStateOf(emptyList<ProductDB>()) }

    db.collection("ProductsDB")
        .get()
        .addOnSuccessListener { querySnapshot: QuerySnapshot ->
            // Convert the QuerySnapshot to a list of data objects using toObjects
            data = querySnapshot.toObjects<ProductDB>()
        }

    data.forEach{item ->
        Text(text = item.name)
    }
}

data class ProductDB(val name: String, val type: String, val price: Int, val condition: String, val description: String, val image: String, val publishDate: String, val stock: Int, val tags: String, val technicalSpecs: String){
    constructor(): this("Prueba", "Prueba", 8, "Prueba","Prueba","Prueba","Prueba",8,"Prueba","Prueba",)
}


@Composable
fun MyBodyHome(){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val maxChar = 25

    var value by remember {
        mutableStateOf("")
    }

    if (isPressed)
        MyBodyHome()

    TopAppBar(modifier = Modifier.height(75.dp),
        title = {
            Column() {
                TextField(modifier = Modifier.height(60.dp),
                    value = value,
                    singleLine = true,
                    placeholder = { Text(text = "Search", fontSize = 22.sp) },
                    onValueChange = {
                        if (it.length <= maxChar) value = it
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

    Column (modifier = Modifier
        .padding(16.dp)
        .border(2.dp, Color.Black, shape = RoundedCornerShape(10.dp))
        .shadow(20.dp)
        .background(Color(0xFFEBEBEB))
    )
    {
        Box(
            modifier = Modifier
                .padding(10.dp)
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

private fun prepareProductList(): List<Product> {
    val productList = arrayListOf<Product>()

    // add menu items
    productList.add(Product(name = "Lab Coat", type = "Accesory", price = 30000, icon = Icons.Filled.Star, date = "24/03/2022" ))
    productList.add(Product(name = "Arduino", type = "Product", price = 400000, icon = Icons.Filled.Phone, date = "2/04/2022"))
    productList.add(Product(name = "Pencils", type = "Material", price = 5000, icon = Icons.Filled.Create, date = "6/05/2022"))
    productList.add(Product(name = "Sneakers", type = "Accesory", price = 100000, icon = Icons.Filled.Star, date = "12/06/2022"))
    productList.add(Product(name = "Lab Mask", type = "Accesory", price = 500000, icon = Icons.Filled.Star, date = "18/07/2022"))
    productList.add(Product(name = "Screwdriver Set", type = "Product", price = 20000, icon = Icons.Filled.Phone, date = "21/08/2022"))

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

data class Product(val name: String, val type: String, val price: Int, val icon: ImageVector, val date: String)


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
                        else if(name == "History")
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
    bottomMenuItemsList.add(BottomMenuItem(label = "History", icon = Icons.Filled.ShoppingCart))
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
            readData()
            MyBodyHome()
            MyBottomBar()
        }
    }
}
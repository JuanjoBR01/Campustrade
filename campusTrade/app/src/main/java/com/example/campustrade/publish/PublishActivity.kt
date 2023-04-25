package com.example.campustrade.publish


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.ui.geometry.Size
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.campustrade.LaunchCameraActivity
import com.example.campustrade.R
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.util.concurrent.ExecutorService
lateinit var outputDirectory: File
lateinit var cameraExecutor: ExecutorService

class PublishActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            Column() {
                MyTopBarPublish()
                MyBodyPublish(LocalContext.current)
            }

        }
    }
}


@Composable
fun MyTopBarPublish(){
    val contextForToast = LocalContext.current.applicationContext
    TopAppBar (
        title = {
            Text(text = "Publish")
        },
        navigationIcon = {
            IconButton(onClick = {
                Toast.makeText(contextForToast, "Back Icon Click", Toast.LENGTH_LONG)
                    .show()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
            }
        },
        backgroundColor = Color(0xFFFB8500)
    )
}

@Composable
fun MyBodyPublish(context: Context){

    val prodType = arrayOf("Material", "Product", "Accessory", "Other")

    var expanded by remember {
        mutableStateOf(false)
    }

    var valueType by remember {
        mutableStateOf(prodType[0])
    }

    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    var valueProduct by remember {
        mutableStateOf("")
    }

    var selectedItem by remember{
        mutableStateOf("New")
    }

    var valuePrice by remember {
        mutableStateOf("")
    }

    var valueDescription by remember {
        mutableStateOf("")
    }

    var valueTags by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Row(modifier = Modifier
        .padding(start = 32.dp, end = 32.dp, top = 24.dp, bottom = 8.dp)){
        Image(painter = painterResource(id = R.drawable.baseline_photo_camera_24),
            contentDescription = "Clickable Camera Icon",
            modifier = Modifier
                .height(120.dp)
                .width(120.dp)
                .border(BorderStroke(1.dp, Color.Black))
                .background(Color(0xFFFFB703))
                .clickable(
                    enabled = true,
                    onClickLabel = "Clickable image",
                    onClick = {
                        val intent = Intent(context, LaunchCameraActivity::class.java)
                        context.startActivity(intent)
                    }
                )
        )
        Column(modifier = Modifier
            .padding(start = 16.dp)) {
            Text(text = "Name your product")
            TextField(value = valueProduct,
                singleLine = true,
                shape = RoundedCornerShape(22.dp),
                placeholder={Text(text="Sunglasses...",fontSize = 12.sp,)},onValueChange = { newText ->
                    valueProduct = newText
                },modifier = Modifier
                    .height(45.dp))
            Text(text = "Give it a price")
            TextField(value = valuePrice,
                singleLine = true,
                shape = RoundedCornerShape(22.dp),
                placeholder={Text(text="10000",fontSize = 12.sp,)},onValueChange = { newText2 ->
                    valuePrice = newText2
                },modifier = Modifier
                    .height(45.dp))
        }
    }
    Column(modifier = Modifier
        .padding(start = 32.dp, end = 32.dp, bottom = 12.dp)){
        Text(text = "Say a little more about it")
        TextField(value = valueDescription,
            singleLine = true,
            shape = RoundedCornerShape(22.dp),
            placeholder={Text(text="It is a product that",fontSize = 12.sp,)},onValueChange = { newText ->
                valueDescription = newText
            },modifier = Modifier
                .height(100.dp)
                .fillMaxWidth())

    }
    Column(modifier = Modifier
        .padding(start = 32.dp, end = 32.dp, bottom = 12.dp)){
        Text(text = "What is it's condition")
        Column(
            modifier = Modifier
                .selectableGroup()
        ){
            Row(
                modifier = Modifier
                    .selectable(
                        selected = (selectedItem == "New"),
                        onClick = { selectedItem = "New" },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 4.dp)
            ){
                RadioButtonStyle(selectedItem,"New")
            }
            Row(
                modifier = Modifier
                    .selectable(
                        selected = (selectedItem == "Used"),
                        onClick = { selectedItem = "Used" },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 8.dp)
            ){
                RadioButtonStyle(selectedItem,"Used")
            }
        }
    }
    Column(modifier = Modifier
        .padding(start = 32.dp, end = 32.dp, bottom = 10.dp)){
        Text(text = "Type of product")
        OutlinedTextField(
            value = valueType,
            onValueChange = { valueType = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    mTextFieldSize = coordinates.size.toSize()
                },
            label = {Text("Label")},
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { expanded = !expanded })
            }
        )

        // Create a drop-down menu with list of cities,
        // when clicked, set the Text Field text as the city selected
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
        ) {
            prodType.forEach { label ->
                DropdownMenuItem(onClick = {
                    valueType = label
                    expanded = false
                }) {
                    Text(text = label)
                }
            }
        }
    }

    Column(modifier = Modifier
        .padding(start = 32.dp, end = 32.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.Center){
        Text(text = "Tags")
        TextField(value = valueTags,
            singleLine = true,
            shape = RoundedCornerShape(22.dp),
            placeholder={Text(text="Short Description",fontSize = 12.sp,)},onValueChange = { newText ->
                valueTags = newText
            },modifier = Modifier
                .height(45.dp)
                .fillMaxWidth())
    }
    Column(modifier = Modifier
        .padding(start = 32.dp, end = 32.dp)
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(onClick = {
            // on below line creating an instance of firebase firestore.
            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
            //creating a collection reference for our Firebase Firestore database.
            //val dbCourses: CollectionReference = db.collection("ProductsDB")
            //adding our data to our courses object class.
           // val productOb = ProductObj("",valueProduct,valuePrice.toInt(),valueDescription,selectedItem,valueType,valueTags)
            //below method is use to add data to Firebase Firestore.
           // dbCourses.add(productOb).addOnSuccessListener {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(
                    context,
                    "Your Course has been added to Firebase Firestore",
                    Toast.LENGTH_SHORT
                ).show()
            //}.addOnFailureListener { e ->
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(context, "Fail to add course", Toast.LENGTH_SHORT).show()
           // }
        },
            colors = ButtonDefaults.buttonColors(backgroundColor =  Color(0xFFFB8500))) {
            Text(text = "Publish")
        }
    }
}


@Composable
private fun RadioButtonStyle(selectedItem: String, details:String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth()
            .background(
                color =
                if (selectedItem == details)
                    (Color(0xFFFFB703))
                else
                    Color.LightGray
            )
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(35.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 6.dp, bottom = 6.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    //horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = details,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyTopBar(){
    Column() {
        MyTopBarPublish()
        MyBodyPublish(LocalContext.current)
    }
}
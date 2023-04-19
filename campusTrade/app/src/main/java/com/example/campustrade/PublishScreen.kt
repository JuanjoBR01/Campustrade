package com.example.campustrade

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import java.util.UUID.*
import androidx.compose.material.AlertDialog

class PublishScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            PublishScreenV()
        }
    }
}

@Composable
fun PublishScreenV() {
    TopView()
}

@Composable
fun TopBarPublishScreen(){
    val context = LocalContext.current
    TopAppBar (
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White
    ){
        Row(){
            IconButton(onClick = {
                val intent = Intent(context, HomeActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
            }
        }
        Row(Modifier.fillMaxSize(), Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
            Text(text = "Publish")
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopView() {
    val context = LocalContext.current
    var contentImage = remember{
        mutableStateOf<Uri?>(null)
    }
    var tempImageFilePath = ""
    var tookPic = remember{
        mutableStateOf<Boolean>(false)
    }
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
            uri: Uri? -> contentImage.value = uri
    }
    var tempImageUri:Uri? = null
    val camera = rememberLauncherForActivityResult(TakePicture()){success->
        tookPic.value = success
        if(success){
            contentImage.value = tempImageUri
        }
    }
    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    var prodName by remember {
        mutableStateOf("")
    }

    var prodPrice by remember {
        mutableStateOf("")
    }

    var prodDescr by remember {
        mutableStateOf("")
    }

    var prodTags by remember {
        mutableStateOf("")
    }

    var selectedItem by remember{
        mutableStateOf("New")
    }

    val prodType = arrayOf("Material", "Product", "Accessory", "Other")

    var expanded by remember {
        mutableStateOf(false)
    }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    var valueType by remember {
        mutableStateOf(prodType[0])
    }

    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}

    var isRunning by remember {mutableStateOf(false)}


    Column(Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())) {
        TopBarPublishScreen()
        Row(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFFB8500))
                    .height(150.dp)
                    .width(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
            ) {
                PhotoView(imagePath = contentImage.value, scope = scope, state = state)
            }
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                OutlinedTextField(
                    value = prodName,
                    label = { Text(text = "Product Name") },
                    onValueChange = { newText: String ->
                        if (newText.length <= 30) {
                            prodName = newText
                        }
                    })

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = prodPrice,
                    label = { Text(text = "Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { newPrice: String ->
                        if (newPrice.length <= 9) {
                            prodPrice = newPrice
                        }
                    })
            }

        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().height(90.dp),
                value = prodDescr,
                label = { Text(text = "Description") },
                onValueChange = { newDescrp: String ->
                    if (newDescrp.length <= 250) {
                        prodDescr = newDescrp
                    }
                })
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "What is its condition",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            fontSize = 15.sp
        )


        Row(
            modifier = Modifier
                .selectableGroup()
        ) {
            Row(
                modifier = Modifier
                    .selectable(
                        selected = (selectedItem == "New"),
                        onClick = { selectedItem = "New" },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                RadioButtonStyle(selectedItem, "New")
            }
            Row(
                modifier = Modifier
                    .selectable(
                        selected = (selectedItem == "Used"),
                        onClick = { selectedItem = "Used" },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 8.dp)
            ) {
                RadioButtonStyle(selectedItem, "Used")
            }
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Type of product", fontSize = 15.sp)
            OutlinedTextField(
                value = valueType,
                onValueChange = { valueType = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .onGloballyPositioned { coordinates ->
                        // This value is used to assign to
                        // the DropDown the same width
                        mTextFieldSize = coordinates.size.toSize()
                    },
                label = { Text("Label") },
                trailingIcon = {
                    Icon(icon, "contentDescription",
                        Modifier.clickable { expanded = !expanded })
                }
            )
            // Create a drop-down menu with list of cities,
            // when clicked, set the Text Field text as the city selected
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
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

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 8.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().height(90.dp),
                value = prodTags,
                label = { Text(text = "Tags") },
                onValueChange = { newTags: String ->
                    if (newTags.length <= 250) {
                        prodTags = newTags
                    }
                })
        }

        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    try{
                        val productOb = ProductObj(
                            "",
                            prodName,
                            prodPrice.toInt(),
                            prodDescr,
                            selectedItem,
                            valueType,
                            prodTags
                        )
                        Toast.makeText(context, "Publishing...", Toast.LENGTH_LONG).show()
                        val imgUrl = uploadImageToDataBase(context,contentImage.value,productOb)
                        prodName = ""
                        prodPrice = ""
                        prodDescr = ""
                        selectedItem = ""
                        valueType = ""
                        prodTags=""
                        contentImage.value = null
                    }
                    catch (e: Exception){
                        Toast.makeText(context, "Error While Trying upload. Please Try Again", Toast.LENGTH_LONG).show()
                        prodName = ""
                        prodPrice = ""
                        prodDescr = ""
                        selectedItem = ""
                        valueType = ""
                        prodTags=""
                        contentImage.value = null
                    }

                    },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFB8500))
            ) {
                Text(text = "Publish")
            }

        }
    }

    BottomActionSheet(state = state, scope = scope,
        onTakeImage = {
            if (it) {
                // Camera
                tempImageUri = FileProvider.getUriForFile(
                    context,
                    "com.example.campustrade.provider",
                    createImageFile(context).also {
                        tempImageFilePath = it.absolutePath
                    })
                camera.launch(tempImageUri)
            } else {
                // Gallery
                pickMedia.launch("image/*")
            }

        }
    )
    {

    }

}


private fun createImageFile(context:Context): File {
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("temp_image",".jpg",storageDir)
}


fun uploadImageToDataBase(context: Context, contentImage: Uri?, productOb: ProductObj): String {

    // create the storage reference
    val storageRef = Firebase.storage.reference

    //Transform to bitmap
    val inputStream = context.contentResolver.openInputStream(contentImage!!)
    val bitmp:Bitmap = BitmapFactory.decodeStream(inputStream)

    //Bitmap to bytes
    val outputStream = ByteArrayOutputStream()
    bitmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val bytes = outputStream.toByteArray()

    //Create variable to store url
    var imgUrl = ""

    //Upload to DB
    val storeR = storageRef.child("images/${UUID.randomUUID()}")
    val uploadTask = storeR.putBytes(bytes)

    uploadTask.addOnSuccessListener { taskSnapshot ->
        storeR.downloadUrl.addOnSuccessListener { uri ->
            imgUrl = uri.toString()
            productOb.changeImage(imgUrl)
            uploadProductToDB(productOb,context)
        }
    }
    return imgUrl
}


private fun uploadProductToDB(productOb: ProductObj,context: Context) {
    // on below line creating an instance of firebase firestore.
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    //creating a collection reference for our Firebase Firestore database.
    val dbCourses: CollectionReference = db.collection("ProductsDB")

    //adding our data to our courses object class.
    //below method is use to add data to Firebase Firestore.
    dbCourses.add(productOb).addOnSuccessListener {
       // after the data addition is successful
       // we are displaying a success toast message.
       Log.d(TAG,"Subio----------------------------------------------------------------------")
       Toast.makeText(
                context,
                "Your Product has been added to Firebase Firestore",
                Toast.LENGTH_LONG
            ).show()
        }.addOnFailureListener { e ->
            // this method is called when the data addition process is failed.
            // displaying a toast message when data addition is failed.
            Toast.makeText(context, "Fail to add product", Toast.LENGTH_LONG).show()
        }
    }




@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PhotoView(modifier: Modifier = Modifier, imagePath:Uri?, scope: CoroutineScope, state: ModalBottomSheetState){
    Image(
        painter = if(imagePath == null){
            painterResource(id = R.drawable.camera)
        }
        else{
            rememberAsyncImagePainter(
                model = imagePath,//ImageRequest.Builder(context = LocalContext.current)
                //.crossfade(true).data(imagePath).build(),
                filterQuality = FilterQuality.High
            )
        },
        contentDescription = null,
        modifier = modifier
            .height(148.dp)
            .width(148.dp)
            .padding(40.dp)
            .clickable {
                scope.launch {
                    state.show()
                }
            }
            .background(color = Color.Transparent),
        contentScale = if(imagePath == null){
            ContentScale.Inside
        }
        else{
            ContentScale.FillWidth
        }
    )

}



@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomActionSheet(state:ModalBottomSheetState, scope: CoroutineScope,onTakeImage:(isCamera:Boolean)-> Unit, modalBottomSheetLayoutScope: @Composable ()->Unit ){
    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            Column(modifier = Modifier.fillMaxHeight()){
                BottomActionItem(title = "Camera", resource = R.drawable.baseline_photo_camera_24, isCamera = true){
                        isCamera ->
                    scope.launch {
                        state.hide()
                    }
                    onTakeImage(isCamera)
                }

                BottomActionItem(title = "Gallery", resource = R.drawable.gallery, isCamera = false){
                        isCamera ->
                    scope.launch {
                        state.hide()
                    }
                    onTakeImage(isCamera)
                }

            }
        }) {
        modalBottomSheetLayoutScope()
    }

}


@Composable
private fun BottomActionItem(modifier: Modifier = Modifier, title:String, resource:Int, isCamera:Boolean,onTakeImage:(isCamera:Boolean)-> Unit){
    Row(modifier = modifier
        .fillMaxWidth()
        .height(50.dp)
        .clickable {
            onTakeImage(isCamera)
        }){
        Image(painter = painterResource(id = resource),
            contentDescription = null,
            modifier = modifier
                .size(40.dp)
                .align(Alignment.CenterVertically),
            contentScale = ContentScale.Inside
        )

        Spacer(modifier = modifier.width(10.dp))

        Text(text = title, modifier = modifier.align(Alignment.CenterVertically))
    }

}


@Composable
private fun RadioButtonStyle(selectedItem: String, details:String) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Box(
        modifier = Modifier
            .padding( top = 4.dp, start = 8.dp, bottom = 4.dp, end = 8.dp)
            .width((screenWidth/2)-16.dp)
            // .fillMaxWidth()
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
        Row(){//modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 6.dp, bottom = 6.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    horizontalArrangement = Arrangement.SpaceBetween
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


fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    return outputStream.toByteArray()
}

@Preview(showBackground = true)
@Composable
fun PreviewPublishScreen(){
    PublishScreenV()
}
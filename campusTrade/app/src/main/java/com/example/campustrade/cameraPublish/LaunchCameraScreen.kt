package com.example.campustrade.cameraPublish

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.campustrade.R
import com.example.campustrade.prodsProfile.ProdsPScreenActivity
import com.example.campustrade.publish.PublishScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.*
import java.io.File

@Composable
fun LaunchCameraScreenV(launchCameraViewModel: LaunchCameraViewModel)
{
    Column(Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())){
        TopBarLaunchCamera()
        BodyLaunchCamera(launchCameraViewModel)
    }

}

@Composable
fun TopBarLaunchCamera(){
    val context = LocalContext.current
    TopAppBar (
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White
    ){
        Row(){
            IconButton(onClick = {
                val intent = Intent(context, PublishScreen::class.java)
                context.startActivity(intent)
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
            }
        }
        Row(Modifier.fillMaxSize(), Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
            Text(text = "Use Your Camera!")
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BodyLaunchCamera(viewModel: LaunchCameraViewModel){

    //Atributo de la imagen a mostrar
    val contentImageLC: Uri? by viewModel.contentImageLC.observeAsState(initial = null)


    UpperLaunchCamera()
    Column(modifier = Modifier
        .padding(horizontal = 90.dp, vertical = 90.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        CenterImage(imagePath = contentImageLC)
    }
    FeatureThatRequiresPermissions()
    val context = LocalContext.current
    var tempImageFilePath = ""
    var tookPic = remember{
        mutableStateOf<Boolean>(false)
    }
    var tempImageUri:Uri? = null
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()){ success->
        tookPic.value = success
        if(success){
            viewModel.onChangeContentImg(tempImageUri)
        }
    }

    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            tempImageUri = FileProvider.getUriForFile(
                context,
                "com.example.campustrade.provider",
                createImageFile(context).also {
                    tempImageFilePath = it.absolutePath
                })
            camera.launch(tempImageUri)
        }) {
            Text("Take Picture")
        }
        Button(onClick = {
            val inputStream = context.contentResolver.openInputStream(contentImageLC!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            if(!isImageTooBrightOrOpaque(bitmap)){
                Toast.makeText(context, "Congrats! Great Image", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(context, "Image is too opaque or too bright", Toast.LENGTH_LONG).show()
            }
        }) {
            Text("Analyze Pictures Brightness and Opaque")
        }
        Button(onClick = {
            val param1 = contentImageLC
            val intent = Intent(context, PublishScreen::class.java).apply {
                putExtra("imgUris", param1.toString())
            }
            context.startActivity(intent)
        }) {
            Text("Use Picture")
        }

    }

}


@Composable
fun UpperLaunchCamera(){
    Column(modifier = Modifier
        .padding(horizontal = 90.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Use your camera", fontWeight = FontWeight.Bold, fontSize = 52.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun CenterImage(modifier: Modifier = Modifier, imagePath:Uri?){
    Image(
        painter = if(imagePath == null){
            painterResource(id = R.drawable.camera)
        }
        else{
            rememberAsyncImagePainter(
                model = imagePath,
                filterQuality = FilterQuality.High
            )
        },
        contentDescription = null,
        modifier = modifier
            .height(200.dp)
            .width(200.dp)
            .padding(40.dp)
            .background(color = Color.Transparent),
        contentScale = if(imagePath == null){
            ContentScale.Inside
        }
        else{
            ContentScale.FillWidth
        }
    )
}

@Composable
@ExperimentalPermissionsApi
private fun FeatureThatRequiresPermissions() {

    Column(Modifier.fillMaxWidth(),verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        // Camera permission state
        val cameraPermissionState = rememberPermissionState(
            Manifest.permission.CAMERA
        )
        getPermission(perm = cameraPermissionState, permissionText = "Camera Permission")
    }

}

@ExperimentalPermissionsApi
@Composable
private fun getPermission(
    perm: PermissionState,
    permissionText: String
) {
    if(perm.hasPermission){
        Text("$permissionText permission Granted",textAlign = TextAlign.Center,)
    }
    else{
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            val textToShow = "The $permissionText is important for this app. Please grant the permission."
            Text(textToShow, textAlign = TextAlign.Center)
            Button(onClick = { perm.launchPermissionRequest() }) {
                Text("Request $permissionText")
            }
        }
    }
}

fun createImageFile(context: Context): File {
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("temp_image",".jpg",storageDir)
}

fun isImageTooBrightOrOpaque(bitmap: Bitmap): Boolean {
    val pixels = IntArray(bitmap.width * bitmap.height)
    bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

    var n:Float = 0.0F
    var r:Float = 0.0F
    var g:Float = 0.0F
    var b:Float = 0.0F

    for (pixel in pixels) {
        val color = Color(pixel)
        r += color.red
        g += color.green
        b += color.blue
        n += 1
    }

    val averageBrightness = (r + b + g) / (n*3)
    Log.d(TAG, "Red: "+ r.toString() + "-----------------------------------------------" )
    Log.d(TAG, "Blue: "+ b.toString() + "-----------------------------------------------" )
    Log.d(TAG, "Green: "+ g.toString() + "-----------------------------------------------" )
    Log.d(TAG, "N: "+ n.toString() + "-----------------------------------------------" )
    Log.d(TAG, "Avg Brigh "+ averageBrightness.toString() + "-----------------------------------------------" )

    return averageBrightness > 0.85 || averageBrightness < 0.2
}

@Preview(showBackground = true)
@Composable
fun PreviewLaunchCameraScreenV(){
    LaunchCameraScreenV(LaunchCameraViewModel())
}
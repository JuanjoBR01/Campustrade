package com.example.campustrade

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.*
import java.io.File


class LaunchCameraScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            LaunchCameraScreenV()
        }
    }
}


@Composable
fun LaunchCameraScreenV()
{
    Column(Modifier
        .fillMaxWidth()){
        TopBarLaunchCamera()
        BodyLaunchCamera()
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
fun BodyLaunchCamera(){
    var contentImage = remember{
        mutableStateOf<Uri?>(null)
    }

    UpperLaunchCamera()
    Column(modifier = Modifier
        .padding(horizontal = 90.dp, vertical = 90.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        CenterImage(imagePath = contentImage.value)
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
            contentImage.value = tempImageUri
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
        Button(onClick = {  }) {
            Text("Analyze Picture")
        }
        Button(onClick = {
            val param1 = contentImage.value
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



@Preview(showBackground = true)
@Composable
fun PreviewLaunchCameraScreenV(){
    LaunchCameraScreenV()
}
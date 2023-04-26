package com.example.campustrade

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.rememberImagePainter
import com.example.campustrade.publish.cameraExecutor
import com.example.campustrade.publish.outputDirectory
import java.io.File
import java.util.concurrent.Executors


class LaunchCameraActivity : ComponentActivity() {
    private lateinit var photoUri: Uri
    private var shouldShowPhoto: MutableState<Boolean> = mutableStateOf(false)
    private var shouldShowCamera: MutableState<Boolean> = mutableStateOf(false)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.i("camera", "Permission granted")
            shouldShowCamera.value = true
        } else {
            Log.i("camera", "Permission denied")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            requestCameraPermission()
            Column() {
                MyTopBar2()
                UpperCamera()
                CameraIconImage()
                CameraBelow()
            }
            if (shouldShowCamera.value) {
                CameraView(
                    outputDirectory = outputDirectory,
                    executor = cameraExecutor,
                    onImageCaptured = ::handleImageCapture,
                    onError = { Log.e("kilo", "View error:", it) }
                )
            }
            if (shouldShowPhoto.value) {
                Image(
                    painter = rememberImagePainter(photoUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

        }
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }
    fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("cesl", "Permission previously granted")
                shouldShowCamera.value = true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.CAMERA
            ) -> Log.i("cesl", "Show camera permissions dialog")

            else -> requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }


    fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    fun handleImageCapture(uri: Uri) {
        Log.i("kilo", "Image captured: $uri")
        shouldShowCamera.value = false
        photoUri = uri
        shouldShowPhoto.value = true
    }
}

@Composable
fun MyTopBar2(){
    val context = LocalContext.current
    TopAppBar (
        title = {
            Text(text = "")
        },
        navigationIcon = {
            IconButton(onClick = {
                val intent = Intent(context, LaunchCameraActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
            }
        }
    )
}

@Composable
fun UpperCamera(){
    Column(modifier = Modifier
        .padding(start = 90.dp, end = 90.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Use your camera", fontWeight = FontWeight.Bold, fontSize = 52.sp, textAlign = TextAlign.Center)
    }
}


@Composable
fun CameraIconImage(){
    Column(modifier = Modifier
        .padding(start = 90.dp, end = 90.dp, top = 90.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.baseline_photo_camera_24),
            contentDescription = "Image Icon",
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
                )
    }
}

@Composable
fun CameraBelow(){

    val context = LocalContext.current

    Column(modifier = Modifier
        .padding(start = 90.dp, end = 90.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Use your camera to take a clear picture", fontWeight = FontWeight.Bold, fontSize = 22.sp, textAlign = TextAlign.Center)
    }

    Column(modifier = Modifier
        .padding(start = 32.dp, end = 32.dp, top = 50.dp, bottom = 50.dp)
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(onClick = {

        },
            colors = ButtonDefaults.buttonColors(backgroundColor =  Color(0xFFFB8500)),
            modifier = Modifier.height(60.dp).width(350.dp)) {
            Text(text = "Start Scanning")
        }
    }
}





@Preview(showBackground = true)
@Composable
fun DefaultPreviewCameraActivity() {
        Column() {
            MyTopBar2()
            UpperCamera()
            CameraIconImage()
            CameraBelow()
        }
}
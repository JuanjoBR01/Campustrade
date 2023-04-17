package com.example.campustrade


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import androidx.compose.material.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.campustrade.ui.theme.orange

private fun takePhoto(
    filenameFormat: String,
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {

    val photoFile = File(
        outputDirectory,
        SimpleDateFormat(filenameFormat, Locale.US).format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(outputOptions, executor, object: ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {
            Log.e("kilo", "Take photo error:", exception)
            onError(exception)
        }

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = Uri.fromFile(photoFile)
            onImageCaptured(savedUri)
        }
    })


}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}


@Composable
fun CameraViewProfile(
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    // 1
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    // 2
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    // 3
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

        Row {


            Button(
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .height(100.dp)
                    .width(100.dp)
                    .background(color = MaterialTheme.colors.secondary),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = orange
                ),
                onClick = {
                    Log.i("kilo", "ON CLICK")
                    takePhoto(
                        filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                        imageCapture = imageCapture,
                        outputDirectory = outputDirectory,
                        executor = executor,
                        onImageCaptured = onImageCaptured,
                        onError = onError
                    )
                },
                content = {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_add_a_photo_black_48),
                        contentDescription = stringResource(id = R.string.add_a_photo_button)
                    )
                }
            )

            Button(
                modifier = Modifier
                    .padding(bottom = 20.dp, start = 20.dp)
                    .height(100.dp)
                    .width(100.dp)
                    .background(color = MaterialTheme.colors.secondary),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = orange
                ),
                onClick = {
                    val intent = Intent(context, SignUpScreen::class.java)
                    context.startActivity(intent)
                },
                content = {
                    Image(
                        painter = painterResource(id = R.drawable.round_arrow_back_black_48),
                        contentDescription = stringResource(id = R.string.add_a_photo_button)
                    )
                }
            )
        }
    }
}


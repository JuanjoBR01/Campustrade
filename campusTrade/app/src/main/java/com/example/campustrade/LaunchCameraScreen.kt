package com.example.campustrade

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


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



@Preview(showBackground = true)
@Composable
fun PreviewLaunchCameraScreenV(){
    LaunchCameraScreenV()
}
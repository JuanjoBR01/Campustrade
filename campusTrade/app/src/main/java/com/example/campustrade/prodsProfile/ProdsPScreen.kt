package com.example.campustrade.prodsProfile

import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.campustrade.HomeActivity
import com.example.campustrade.R
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.example.campustrade.publish.PublishViewModel
import androidx.activity.viewModels


@Composable
fun ProdsPScreenMain(viewModel: ProdsPViewModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            ){
        TopBarProdsPScreen()
        TopAppProdsView(viewModel)
    }


}

@Composable
fun TopBarProdsPScreen() {
    val context = LocalContext.current
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White
    ) {
        Row() {
            IconButton(onClick = {
                val intent = Intent(context, HomeActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
            }
        }
        Row(
            Modifier.fillMaxSize(),
            Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Find Products by Publisher")
        }
    }
}

@Composable
fun TopAppProdsView(viewModel: ProdsPViewModel){

    //Contexto de la app
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ){
        ImageWithFallback(viewModel.prodImage.value!!,R.drawable.sampleuser)
    }


}

@Composable
fun ImageWithFallback(url: String, fallbackImageResId: Int) {
    Box(modifier = Modifier.aspectRatio(1f)) {
        Image(
            painter = rememberImagePainter(
                data = url,
                builder = {
                    error(fallbackImageResId)
                    fallback(fallbackImageResId)
                    transformations(CircleCropTransformation())
                    memoryCachePolicy(CachePolicy.DISABLED)
                    diskCachePolicy(CachePolicy.READ_ONLY)
                }
            ),
            contentDescription = "Image",
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center
        )
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewProdsPScreen() {
    ProdsPScreenActivity()
}
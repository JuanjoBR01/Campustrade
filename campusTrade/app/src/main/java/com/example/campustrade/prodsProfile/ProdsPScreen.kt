package com.example.campustrade.prodsProfile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.campustrade.HomeActivity
import com.example.campustrade.R
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest



@Composable
fun ProdsPScreenMain(viewModel: ProdsPViewModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            ){
        TopBarProdsPScreen()
        TopAppProdsView(viewModel)
        MiddleProdsViews(viewModel)
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
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ){
        ImageWithFallback(Uri.parse("android.resource://" + context.packageName + "/" + R.drawable.deffff),viewModel)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Alberto Crazy",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(16.dp)
        )
    }
    Spacer(modifier = Modifier.width(4.dp))
}
@Composable
fun MiddleProdsViews(viewModel: ProdsPViewModel){

    ScreenWithArrows(viewModel)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column() {
            Text(
                text = "Lab Mask",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Price",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "Tags",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}




@Composable
fun ScreenWithArrows(viewModel: ProdsPViewModel) {
    val listItems = remember {
        mutableStateListOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    }
    var currentIndex by remember { mutableStateOf(0) }

//Contexto de la app
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_left_arrow),
                contentDescription = "Previous",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { currentIndex = (currentIndex - 1).coerceAtLeast(0) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {

                ImageWithFallback(Uri.parse("android.resource://" + context.packageName + "/" + R.drawable.deffff),viewModel)

            }
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = "Next",
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        currentIndex = (currentIndex + 1).coerceAtMost(listItems.size - 1)
                    }
            )
        }
    }
}

@Composable
fun ImageWithFallback(imageUr: Uri, viewModel: ProdsPViewModel) {
    Image(
        painter =
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(context = LocalContext.current)
                .crossfade(true).data(imageUr).build(),
            filterQuality = FilterQuality.High
        ),
        contentDescription = null,
        modifier = Modifier
            .height(250.dp)
            .width(250.dp)
            .background(color = Color.Transparent),
        contentScale = if (viewModel.prodImage.value == null) {
            ContentScale.Inside
        } else {
            ContentScale.FillWidth
        }
    )
}




@Preview(showBackground = true)
@Composable
fun PreviewProdsPScreen() {
    ProdsPScreenActivity()
}
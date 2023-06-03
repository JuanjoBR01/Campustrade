package com.example.campustrade.prodsProfile

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.campustrade.HomeActivity
import com.example.campustrade.R
import com.example.campustrade.cameraPublish.LaunchCameraScreen
import com.example.campustrade.dtos.ProductObj
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.UUID.*
import androidx.compose.runtime.livedata.observeAsState as observeAsState


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

    //Modelo
    val valueType = viewModel.giveUserNames()

    //Valor seleccionado en el combobox
    val userN: String by viewModel.userName.observeAsState(initial = "")

    //Tamaño del textfield
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    //Si esta expandida el combobox
    val expanded: Boolean by viewModel.expanded.observeAsState(initial = false)

    //Imagen URL
    val imageUrl: String by viewModel.prodImage.observeAsState(initial = "")


    //Cambia el icono dependiendo del valor de expandido
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Spacer(modifier = Modifier.width(4.dp))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
                builder = {
                    crossfade(true)
                    placeholder(R.drawable.programmer)
                    error(R.drawable.programmer)
                }
            ),
            contentDescription = "Imagen",
            modifier = Modifier.rotate(90f).height(120.dp).clip(CircleShape)
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = userN,
            readOnly = true,
            onValueChange = { viewModel.onChangeComboBox(it, expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .onGloballyPositioned { coordinates ->
                    mTextFieldSize = coordinates.size.toSize()
                },
            label = { Text("User Name") },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { viewModel.onChangeComboBox(userN, !expanded) })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { viewModel.onChangeComboBox(userN, false) },
            modifier = Modifier
                .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
        ) {
            valueType.forEach { label ->
                DropdownMenuItem(onClick = {
                    viewModel.onChangeComboBox(label, false)
                }) {
                    Text(text = label)
                }
            }
        }
    }


    Spacer(modifier = Modifier.width(4.dp))


    //Nombre del producto
    val prodName: String by viewModel.prodName.observeAsState(initial = "")

    //Nombre del producto
    val prodPrice: String by viewModel.prodPrice.observeAsState(initial = "")

    //Nombre del producto
    val prodTags: String by viewModel.prodTag.observeAsState(initial = "")

    //Imagen URL
    val imageUrlProd: String by viewModel.prodImgg.observeAsState(initial = "")

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
                    .clickable { viewModel.minusIndex() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Image(
                    painter = rememberImagePainter(
                        data = imageUrlProd,
                        builder = {
                            crossfade(true)
                            placeholder(R.drawable.noprod)
                            error(R.drawable.noprod)
                        }
                    ),
                    contentDescription = "Imagen",
                    modifier = Modifier.height(120.dp).clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = "Next",
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        viewModel.plusIndex()
                        Log.d(TAG,"Entro a dar click derecho")
                    }
            )
        }
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            Text(
                text = "Name: " + prodName,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Price: " + prodPrice,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "Tags: " + prodTags,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}




@Composable
fun ScreenWithArrows(viewModel: ProdsPViewModel) {

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
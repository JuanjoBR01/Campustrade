package com.example.campustrade

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.UUID.*
import java.text.SimpleDateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PublishScreenV(uris: Uri?, viewModel: PublishViewModel) {
    //Atributo Scope
    val scope: CoroutineScope by viewModel.scope.observeAsState(initial = rememberCoroutineScope())

    //Atributo state
    val state: ModalBottomSheetState by viewModel.state.observeAsState(
        initial = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden
        )
    )

    //Pantalla de cargando datos
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Uploading product to DataBase", style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            )
        }
    } else {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            TopBarPublishScreen()
            TopView(uris, viewModel, scope, state)
            MiddleView(viewModel)
            bottomView(viewModel)
        }
        selectableWindow(viewModel, scope, state)
    }
}

@Composable
fun TopBarPublishScreen() {
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
            Text(text = "Publish")
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopView(
    uris: Uri?,
    viewModel: PublishViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    //Atributo nombre del producto a agregar
    val prodName: String by viewModel.prodName.observeAsState(initial = " ")

    //Atributo precio del producto a agregar
    val prodPrice: String by viewModel.prodPrice.observeAsState(initial = " ")

    //Atributo de la imagen a mostrar
    val contentImage: Uri? by viewModel.contentImage.observeAsState(initial = uris)

    viewModel.onChangeImage(uris)

    Row(modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .background(Color(0xFFFB8500))
                .height(150.dp)
                .width(150.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
        ) {
            PhotoView(imagePath = contentImage, scope = scope, state = state)
        }
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            OutlinedTextField(
                value = prodName,
                label = { Text(text = "Product Name") },
                onValueChange = {
                    viewModel.onPublishChanged(
                        it,
                        prodPrice,
                        viewModel.prodDescr.value!!,
                        viewModel.prodTags.value!!,
                        viewModel.selectedItem.value!!
                    )
                },
                singleLine = true,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = prodPrice,
                label = { Text(text = "Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    viewModel.onPublishChanged(
                        prodName,
                        it,
                        viewModel.prodDescr.value!!,
                        viewModel.prodTags.value!!,
                        viewModel.selectedItem.value!!
                    )
                },
                singleLine = true,
                maxLines = 1
            )
        }
    }
}

@Composable
fun MiddleView(
    viewModel: PublishViewModel
) {
    //Tipos de productos
    val prodType = arrayOf("Material", "Product", "Accessory", "Other")

    //Atributo descripcion del producto a agregar
    val prodDescr: String by viewModel.prodDescr.observeAsState(initial = " ")

    //Atributo new or old del producto
    val selectedItem: String by viewModel.selectedItem.observeAsState(initial = "New")

    //Valor seleccionado en el combobox
    val valueType: String by viewModel.valueType.observeAsState(initial = prodType[0])

    //Si esta expandida el combobox
    val expanded: Boolean by viewModel.expanded.observeAsState(initial = false)

    //Cambia el icono dependiendo del valor de expandido
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    //Tamaño del textfield
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            value = prodDescr,
            label = { Text(text = "Description") },
            onValueChange = {
                viewModel.onPublishChanged(
                    viewModel.prodName.value!!,
                    viewModel.prodPrice.value!!,
                    it,
                    viewModel.prodTags.value!!,
                    selectedItem
                )
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
                    onClick = {
                        viewModel.onPublishChanged(
                            viewModel.prodName.value!!,
                            viewModel.prodPrice.value!!,
                            prodDescr,
                            viewModel.prodTags.value!!,
                            "New"
                        )
                    },
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
                    onClick = {
                        viewModel.onPublishChanged(
                            viewModel.prodName.value!!,
                            viewModel.prodPrice.value!!,
                            prodDescr,
                            viewModel.prodTags.value!!,
                            "Used"
                        )
                    },
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
            readOnly = true,
            onValueChange = { viewModel.onChangeComboBox(it, expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .onGloballyPositioned { coordinates ->
                    mTextFieldSize = coordinates.size.toSize()
                },
            label = { Text("Label") },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { viewModel.onChangeComboBox(valueType, !expanded) })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { viewModel.onChangeComboBox(valueType, false) },
            modifier = Modifier
                .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
        ) {
            prodType.forEach { label ->
                DropdownMenuItem(onClick = {
                    viewModel.onChangeComboBox(label, false)
                }) {
                    Text(text = label)
                }
            }
        }
    }

}


@Composable
fun bottomView(viewModel: PublishViewModel) {

    //Contexto de la aplicacion
    val context = LocalContext.current

    //Atributo de los tags del producto a agregar
    val prodTags: String by viewModel.prodTags.observeAsState(initial = "")

    //Atributo de la fecha actual
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDate = Date()
    val publishDat = dateFormat.format(currentDate)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 8.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            value = prodTags,
            label = { Text(text = "Tags") },
            onValueChange = {
                viewModel.onPublishChanged(
                    viewModel.prodName.value!!,
                    viewModel.prodPrice.value!!,
                    viewModel.prodDescr.value!!,
                    it,
                    viewModel.selectedItem.value!!
                )
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
                try {
                    Log.d(
                        TAG,
                        "PreProd----------------------------------------------------------------------------------------------"
                    )
                    val productOb = ProductObj(
                        image = " ",
                        name = viewModel.prodName.value!!,
                        price = viewModel.prodPrice.value!!.toInt(),
                        description = viewModel.prodDescr.value!!,
                        condition = viewModel.selectedItem.value!!,
                        type = viewModel.valueType.value!!,
                        tags = prodTags,
                        publishDate = publishDat,
                        stock = 1,
                        technicalSpecs = "TS"
                    )
                    Log.d(
                        TAG,
                        "PostProd----------------------------------------------------------------------------------------------"
                    )
                    Toast.makeText(context, "Publishing...", Toast.LENGTH_LONG).show()
                    Log.d(
                        TAG,
                        "PreUpload----------------------------------------------------------------------------------------------"
                    )
                    Log.d(TAG, viewModel.contentImage.value.toString())
                    viewModel.onChangeIsLoading(true)
                    viewModel.uploadImageToDataBase(context, productOb)
                    //Clear output
                    viewModel.onPublishChanged("", "", "", "", "Used")
                    viewModel.onChangeComboBox("", viewModel.expanded.value!!)
                    Log.d(ContentValues.TAG, "Clear output")
                } catch (e: Exception) {
                    Log.d(
                        TAG,
                        "----------------------------------------------------------------------------------------------"
                    )
                    Log.d(TAG, e.toString())
                    Log.d(TAG, e.message.toString())
                    Toast.makeText(
                        context,
                        "Error While Trying upload. Please Try Again",
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.onPublishChanged("", "", "", "", "Used")
                    viewModel.onChangeComboBox("", viewModel.expanded.value!!)
                    viewModel.onChangeImage(null)
                }

            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFB8500))
        ) {
            Text(text = "Publish")
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun selectableWindow(
    viewModel: PublishViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    //Context of the application
    val context = LocalContext.current

    //Window that allows photos to be picked from the gallery
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewModel.onChangeImage(uri)
        }

    BottomActionSheet(state = state, scope = scope,
        onTakeImage = {
            if (it) {
                //Camera
                val intent = Intent(context, LaunchCameraScreen::class.java)
                context.startActivity(intent)
            } else {
                // Gallery
                pickMedia.launch("image/*")
            }

        }
    )
    {

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PhotoView(
    modifier: Modifier = Modifier,
    imagePath: Uri?,
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    Image(
        painter = if (imagePath == null) {
            painterResource(id = R.drawable.camera)
        } else {
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
        contentScale = if (imagePath == null) {
            ContentScale.Inside
        } else {
            ContentScale.FillWidth
        }
    )

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomActionSheet(
    state: ModalBottomSheetState,
    scope: CoroutineScope,
    onTakeImage: (isCamera: Boolean) -> Unit,
    modalBottomSheetLayoutScope: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            Column(modifier = Modifier.fillMaxHeight()) {
                BottomActionItem(
                    title = "Camera",
                    resource = R.drawable.baseline_photo_camera_24,
                    isCamera = true
                ) { isCamera ->
                    scope.launch {
                        state.hide()
                    }
                    onTakeImage(isCamera)
                }

                BottomActionItem(
                    title = "Gallery",
                    resource = R.drawable.gallery,
                    isCamera = false
                ) { isCamera ->
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
private fun BottomActionItem(
    modifier: Modifier = Modifier,
    title: String,
    resource: Int,
    isCamera: Boolean,
    onTakeImage: (isCamera: Boolean) -> Unit
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .height(50.dp)
        .clickable {
            onTakeImage(isCamera)
        }) {
        Image(
            painter = painterResource(id = resource),
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
private fun RadioButtonStyle(selectedItem: String, details: String) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Box(
        modifier = Modifier
            .padding(top = 4.dp, start = 8.dp, bottom = 4.dp, end = 8.dp)
            .width((screenWidth / 2) - 16.dp)
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
        Row() {//modifier = Modifier.fillMaxWidth()) {
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

@Preview(showBackground = true)
@Composable
fun PreviewPublishScreen() {
    PublishScreenV(null, PublishViewModel())
}
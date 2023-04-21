package com.example.campustrade
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.Icon
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import com.example.campustrade.ui.theme.CampustradeTheme
import com.example.campustrade.ui.theme.darkBlue
import com.example.campustrade.ui.theme.orange
import com.example.campustrade.ui.theme.white
import java.text.SimpleDateFormat
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
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


class SignUpScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampustradeTheme {
                SignUpScreenComposable(viewModel = SignUpViewModel(SignUpRepository()))
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SignUpScreenComposable(modifier: Modifier = Modifier, viewModel: SignUpViewModel) {
    //var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
    val prodType = arrayOf("Material", "Product", "Accessory", "Other")

    val expanded: Boolean by viewModel.expanded.observeAsState(initial = false)
    val valueType: String by viewModel.valueType.observeAsState(initial = prodType[0])
    val nameField: String by viewModel.name.observeAsState(initial = "")
    val emailField: String by viewModel.email.observeAsState(initial = "")
    val secretField: String by viewModel.password.observeAsState(initial = "")
    val confirmSecretField: String by viewModel.confirmPassword.observeAsState(initial = "")

    val context = LocalContext.current

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDate = Date()
    val publishDat = dateFormat.format(currentDate)


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
    var tempImageUri: Uri? = null
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()){ success->
        tookPic.value = success
        if(success){
            contentImage.value = tempImageUri
        }
    }

    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()


    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        TopAppBar(
            title = { Text(
                stringResource(R.string.sign_up_top_bar_text),
                style = MaterialTheme.typography.h2) },
            modifier = modifier,
            navigationIcon = {
                IconButton(
                    onClick = { val intent = Intent(context, LoginScreen::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            },
            backgroundColor = white,
            contentColor = Color.Black,
        )

        Spacer(modifier = Modifier.height(25.dp))

        Box(
            modifier = Modifier
                .background(Color(0xFFFB8500))
                .height(120.dp)
                .width(120.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            PhotoView2(imagePath = contentImage.value, scope = scope, state = state)
        }


        Spacer(modifier = Modifier.height(10.dp))

        TextBoxField(
            nameField,
            {
                    if(it.length < 51) {
                        viewModel.onNameChange(it)
                    }
            },
            stringResource(id = R.string.name_sign_up_form),
            VisualTransformation.None)

        Spacer(modifier = Modifier.height(10.dp))

        TextBoxField(
            emailField,
            {newValue ->
                if (newValue.length <41){
                    viewModel.onEmailChange(newValue)
                }},
            label = stringResource(id = R.string.email_sign_up_form),
            VisualTransformation.None
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(modifier = Modifier
            .padding(start = 32.dp, end = 32.dp, bottom = 10.dp)){
            OutlinedTextField(
                value = valueType,
                onValueChange = {
                    if (it.length < 41){
                        viewModel.onValueTypeChange(it)
                    }
                                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { //coordinates ->
                        // This value is used to assign to
                        // the DropDown the same width
                        // mTextFieldSize = coordinates.size.toSize()
                    },
                label = {Text("Label")},
                trailingIcon = {
                    Icon(painter = painterResource(id = R.drawable.baseline_expand_more_black_48),"contentDescription",
                        Modifier.clickable { viewModel.onExpandedChange() })
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { viewModel.setExpandedFalse() },
                modifier = Modifier
                    .width(100.dp)
                    .background(Color.White)

            ) {
                prodType.forEach { label ->
                    DropdownMenuItem(onClick = {
                        viewModel.onValueTypeChange(label)
                        viewModel.setExpandedFalse()
                    }) {
                        Text(text = label,
                            modifier
                                .fillMaxSize(),
                            style = MaterialTheme.typography.body1,
                            color = Color.Black)
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(10.dp))

        TextBoxField(
            secretField,
            {newValue ->
                if (newValue.length < 51) {
                    viewModel.onPasswordChange(newValue)
                }},
            label = stringResource(id = R.string.password_sign_up_form),
            PasswordVisualTransformation())

        Spacer(modifier = Modifier.height(10.dp))

        TextBoxField(confirmSecretField,
            {newValue ->
                if (newValue.length < 51) {
                    viewModel.onConfirmPasswordChange(newValue)
                }},
            label = stringResource(id = R.string.confirm_password_sign_up_form),
            PasswordVisualTransformation())

        Spacer(modifier = Modifier.height(20.dp))



        Button(
            onClick = {
                var aux: Boolean
                var mes: String
                if (secretField.length < 6 || confirmSecretField.length < 6) {
                    mes = "The password needs at least 6 characters"
                } else if (secretField != confirmSecretField) {
                    mes = "The password fields do not match"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailField).matches()) {
                    mes = "Enter a valid email"
                } else if (nameField.isEmpty()) {
                    mes = "Enter a valid name"
                } else if (contentImage.value == null){
                    mes = "Please take a photo"
                } else {

                    aux = uploadImageToDB(context, contentImage.value, viewModel,
                        valueType, nameField, emailField, secretField)
                    mes = if (aux) {
                        "User created in Auth system and FireStore DB"
                    } else {
                        "Failed to create user in the DB"
                    }

                    viewModel.restartForm()
                }

                Toast.makeText(
                    context,
                    mes,
                    Toast.LENGTH_LONG
                ).show()

                },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = orange
            ),
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 6.dp,
                pressedElevation = 8.dp,
                disabledElevation = 0.dp
            )

        ) {
            Text(stringResource(id = R.string.confirm_button_sign_up_form),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.background)
        }

        BottomActionSheet2(state = state, scope = scope,
            onTakeImage = {
                if (it) {
                    // Camera
                    tempImageUri = FileProvider.getUriForFile(
                        context,
                        "com.example.campustrade.provider",
                        createImageFile2(context).also {
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

}

@Composable
fun TextBoxField(value: String, onValueChange: (String) -> Unit, label: String, visualTransformation: VisualTransformation) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange ,
        label = { Text(text = label, color = darkBlue, style = MaterialTheme.typography.body1) },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = darkBlue,
            unfocusedBorderColor = darkBlue),
        modifier = Modifier.width(350.dp),
        visualTransformation = visualTransformation

    )

}




fun createImageFile2(context:Context): File {
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("temp_image",".jpg",storageDir)
}


fun uploadImageToDB(context: Context, contentImage: Uri?, viewModel: SignUpViewModel, vt: String, nn: String, em: String, pw: String): Boolean {
    // create the storage reference
    var aux = true
    aux = viewModel.uploadImage(context, contentImage, viewModel, vt, nn, em, pw)

    return aux


}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PhotoView2(modifier: Modifier = Modifier, imagePath:Uri?, scope: CoroutineScope, state: ModalBottomSheetState){
    Image(
        painter = if(imagePath == null){
            painterResource(id = R.drawable.baseline_add_a_photo_black_48)
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
fun BottomActionSheet2(state:ModalBottomSheetState, scope: CoroutineScope,onTakeImage:(isCamera:Boolean)-> Unit, modalBottomSheetLayoutScope: @Composable ()->Unit ){
    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            Column(modifier = Modifier.fillMaxHeight()){
                BottomActionItem2(title = "Camera", resource = R.drawable.baseline_photo_camera_24, isCamera = true){
                        isCamera ->
                    scope.launch {
                        state.hide()
                    }
                    onTakeImage(isCamera)
                }

                BottomActionItem2(title = "Gallery", resource = R.drawable.gallery, isCamera = false){
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
private fun BottomActionItem2(modifier: Modifier = Modifier, title:String, resource:Int, isCamera:Boolean,onTakeImage:(isCamera:Boolean)-> Unit){
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




@Preview
@Composable
fun SignUpScreenPreview() {
    CampustradeTheme {
        SignUpScreenComposable(viewModel = SignUpViewModel(SignUpRepository()))
    }
}
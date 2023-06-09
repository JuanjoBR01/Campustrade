package com.example.campustrade.signup
import android.Manifest
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
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.campustrade.ConnectivityReceiver
import com.example.campustrade.R
import com.example.campustrade.data.Resource
import com.example.campustrade.home.HomeActivityMVVM
import com.example.campustrade.login.LoginScreen
import com.example.campustrade.objects.SignUpTime
import com.example.campustrade.repository.AuthenticationRepository
import com.example.campustrade.ui.theme.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import java.util.UUID.*


class SignUpScreen : ComponentActivity() {

    //private val viewModel by viewModels<SignUpViewModel>()
    private val viewModel = SignUpViewModel(AuthenticationRepository(FirebaseAuth.getInstance()))
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        SignUpTime.startTime = SystemClock.elapsedRealtime()
        super.onCreate(savedInstanceState)
        setContent {
            CampustradeTheme {
                SignUpScreenComposable(viewModel = viewModel)
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun SignUpScreenComposable(modifier: Modifier = Modifier, viewModel: SignUpViewModel) {
    val prodType = arrayOf("Material", "Product", "Accessory", "Other")

    val expanded: Boolean by viewModel.expanded.observeAsState(initial = false)
    val valueType: String by viewModel.valueType.observeAsState(initial = prodType[0])
    val nameField: String by viewModel.name.observeAsState(initial = "")
    val emailField: String by viewModel.email.observeAsState(initial = "")
    val secretField: String by viewModel.password.observeAsState(initial = "")
    val confirmSecretField: String by viewModel.confirmPassword.observeAsState(initial = "")

    val context = LocalContext.current

    val signUpFlow = viewModel?.signUpFlow?.collectAsState()

    val connectivityReceiver = remember { ConnectivityReceiver(context = context) }
    connectivityReceiver.register()

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
    ) {

        TopAppBar(
            title = {
                Text(
                    stringResource(R.string.sign_up_top_bar_text),
                    style = MaterialTheme.typography.h2
                )
            },
            modifier = modifier,
            navigationIcon = {
                IconButton(
                    onClick = {
                        val intent = Intent(context, LoginScreen::class.java)
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
        ) {
            PhotoView2(imagePath = contentImage.value, scope = scope, state = state)
        }


        Spacer(modifier = Modifier.height(10.dp))

        TextBoxField(
            nameField,
            {
                if (it.length < 51) {
                    viewModel.onNameChange(it)
                }
            },
            stringResource(id = R.string.name_sign_up_form),
            VisualTransformation.None
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextBoxField(
            emailField,
            { newValue ->
                if (newValue.length < 41) {
                    viewModel.onEmailChange(newValue)
                }
            },
            label = stringResource(id = R.string.email_sign_up_form),
            VisualTransformation.None
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .padding(start = 32.dp, end = 32.dp, bottom = 10.dp)
        ) {
            OutlinedTextField(
                value = valueType,
                onValueChange = {
                    if (it.length < 41) {
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
                label = { Text("Label") },
                trailingIcon = {
                    Icon(painter = painterResource(id = R.drawable.baseline_expand_more_black_48),
                        "contentDescription",
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
                        Text(
                            text = label,
                            modifier
                                .fillMaxSize(),
                            style = MaterialTheme.typography.body1,
                            color = Color.Black
                        )
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(15.dp))

        TextBoxField(
            secretField,
            { newValue ->
                if (newValue.length < 51) {
                    viewModel.onPasswordChange(newValue)
                }
            },
            label = stringResource(id = R.string.password_sign_up_form),
            PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextBoxField(
            confirmSecretField,
            { newValue ->
                if (newValue.length < 51) {
                    viewModel.onConfirmPasswordChange(newValue)
                }
            },
            label = stringResource(id = R.string.confirm_password_sign_up_form),
            PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(15.dp))


        NoInternetText(context = context)

        FeatureThatRequiresPermissions()

        Button(
            onClick = {
                if (connectivityReceiver.isOnline) {
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
                        mes = "Uploading info..."
                        SignUpTime.endTime = SystemClock.elapsedRealtime()
                        SignUpTime.totalTime = SignUpTime.endTime - SignUpTime.startTime

                        viewModel?.uploadImage(context, contentImage.value, valueType, nameField, emailField, secretField)


                        viewModel.restartForm()
                    }


                    Toast.makeText(
                        context,
                        mes,
                        Toast.LENGTH_LONG
                    ).show()
                }



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
        ) { }

        signUpFlow?.value?.let {
            when(it) {
                is Resource.Failure ->{
                    val context = LocalContext.current
                    Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))

                }
                is Resource.Success -> {
                    LaunchedEffect(Unit) {
                        val intent = Intent(context, HomeActivityMVVM::class.java)
                        context.startActivity(intent)
                    }
                }
                Resource.PastFailure -> {
                    println("Just failed")
                }
            }
        }

        DisposableEffect(key1 = connectivityReceiver) {
            onDispose {
                connectivityReceiver.unregister()
            }
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

        Text(text = title, modifier = modifier.align(Alignment.CenterVertically))
    }

}

@Composable
fun NoInternetText(context: Context) {
    val connectivityReceiver = remember { ConnectivityReceiver(context = context) }
    connectivityReceiver.register()

    if (!connectivityReceiver.isOnline) {
        Text(text = "Disconnected, please check your internet connection!",
            color = Color.Red,
            style = MaterialTheme.typography.body2,

            )
        Spacer(modifier = Modifier.height(20.dp))

        Log.d("ConnectionEvent", "Houston, we lost connectivity")
    }

    DisposableEffect(key1 = connectivityReceiver) {
        onDispose {
            connectivityReceiver.unregister()
        }
    }
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
        GetPermission(perm = cameraPermissionState)
    }

}

@ExperimentalPermissionsApi
@Composable
private fun GetPermission(
    perm: PermissionState
) {
    if(perm.hasPermission){
        print("Permission already granted")
    }
    else{

        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { perm.launchPermissionRequest()},
                colors = ButtonDefaults.buttonColors(backgroundColor = orange),
                modifier = Modifier.width(200.dp).height(50.dp)){
                Text("Request Camera Permission", color = black
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}
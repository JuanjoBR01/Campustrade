package com.example.campustrade
import android.content.Intent
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
//import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
//import androidx.compose.ui.unit.toSize
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import com.example.campustrade.ui.theme.CampustradeTheme
import com.example.campustrade.ui.theme.darkBlue
import com.example.campustrade.ui.theme.orange
import com.example.campustrade.ui.theme.white
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class SignUpScreen : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.i("camera", "Permission granted")
        } else {
            Log.i("camera", "Permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampustradeTheme {
                SignUpScreenComposable(viewModel = SignUpViewModel())
            }
        }
    }

}

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

        Button(onClick = {val intent = Intent(context, LaunchCameraProfileActivity::class.java)
            context.startActivity(intent)
        },
            modifier = Modifier
                .width(80.dp)
                .height(80.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = orange)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_add_a_photo_black_48),
                contentDescription = stringResource(id = R.string.add_a_photo_button)
            )
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
                var aux = false
                var mes = ""
                if (secretField.length < 6 || confirmSecretField.length < 6) {
                    mes = "The password needs at least 6 characters"
                } else if (secretField != confirmSecretField) {
                    mes = "The password fields do not match"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailField).matches()) {
                    mes = "Enter a valid email"
                } else if (nameField.isEmpty()) {
                    mes = "Enter a valid name"
                } else {
                    aux = viewModel.createUser(valueType, nameField, emailField, secretField)
                    mes = if (aux) {
                        "User created in Auth system and FireStore DB"
                    } else {
                        "Failed to create user in the DB"
                    }
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
@Preview
@Composable
fun SignUpScreenPreview() {
    CampustradeTheme {
        SignUpScreenComposable(viewModel = SignUpViewModel())
    }
}
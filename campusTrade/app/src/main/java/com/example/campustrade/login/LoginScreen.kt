package com.example.campustrade.login


import androidx.compose.foundation.Image
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import android.content.Intent
import androidx.compose.ui.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.example.campustrade.home.HomeActivityMVVM
import com.example.campustrade.R
import com.example.campustrade.signup.SignUpScreen
import com.example.campustrade.ui.theme.CampustradeTheme
import com.example.campustrade.ui.theme.darkBlue
import com.example.campustrade.ui.theme.orange
import com.example.campustrade.ui.theme.yellow
import com.example.campustrade.data.Resource
import com.example.campustrade.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campustrade.ConnectivityReceiver

class LoginScreen : ComponentActivity() {
    //private val viewModel by viewModels<LoginViewModel>()
    @RequiresApi(Build.VERSION_CODES.O)
    private val viewModel = LoginViewModel(AuthenticationRepository(FirebaseAuth.getInstance()))

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CampustradeTheme{
                LoginScreenComposable(viewModel = viewModel)
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreenComposable(modifier: Modifier = Modifier, viewModel: LoginViewModel) {

    val emailField: String by viewModel.email.observeAsState(initial = "")
    val passwordField: String by viewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)
    val signupEnabled: Boolean by viewModel.signupEnable.observeAsState(initial = true)
    val launchedTime: Boolean by viewModel.launchedTime.observeAsState(initial = false)
    val loginFlow = viewModel.loginFlow.collectAsState()

    val context = LocalContext.current


    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
    ){
        Text(
            text = stringResource(id = R.string.app_title),
            style = MaterialTheme.typography.h1,
            modifier = modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally),
            color = darkBlue,

            )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = emailField,
            onValueChange = {
                if (it.length<41)
                    viewModel.onLoginChanged(it, passwordField)
                            },
            label = { Text(stringResource(id = R.string.user_login_view),
                color = darkBlue,
                style = MaterialTheme.typography.body1) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    tint = darkBlue,
                    contentDescription = stringResource(id = R.string.person_icon)
                ) },
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = orange,
                unfocusedBorderColor = darkBlue)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = passwordField,
            onValueChange = {
                if (it.length < 51)
                    viewModel.onLoginChanged(emailField, it)
                            },
            label = { Text(stringResource(id = R.string.password_login_view),
                color = darkBlue) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    tint = darkBlue,
                    contentDescription = stringResource(id = R.string.lock_icon)
                ) },
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = orange,
                unfocusedBorderColor = darkBlue),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                      viewModel.login(emailField, passwordField, context)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = orange,
                disabledBackgroundColor = Color(0xFF535D64)
            ),
            modifier = Modifier.width(200.dp),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 6.dp,
                pressedElevation = 8.dp,
                disabledElevation = 0.dp
            ),
            enabled = loginEnable

        ) {
            Text(stringResource(id = R.string.log_in_button),
                color = MaterialTheme.colors.background)
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = { val intent = Intent(context, SignUpScreen::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = yellow
            ),
            modifier = Modifier.width(200.dp),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 6.dp,
                pressedElevation = 8.dp,
                disabledElevation = 0.dp
            ),
            enabled = signupEnabled

        ) {
            Text(stringResource(id = R.string.sign_up_button),
                style = MaterialTheme.typography.body1)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            modifier = Modifier
                .size(175.dp)
                .clip(RoundedCornerShape(25.dp))
                .align(Alignment.CenterHorizontally),
            painter = painterResource(R.drawable.logo_cubito_cuadrado),
            contentDescription = stringResource(id = R.string.cube_logo),

            )

        NoInternetDialog(context = context)

        loginFlow.value?.let {
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

        if (!launchedTime) {
            viewModel.uploadLaunchTime()
            viewModel.launched()
        }


    }

}

@Composable
fun NoInternetDialog(context: Context) {
    val connectivityReceiver = remember { ConnectivityReceiver(context = context) }
    connectivityReceiver.register()

    if (!connectivityReceiver.isOnline) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Disconnected!") },
            text = { Text("Oops! You aren't connected to internet, so we won't be able to process you login request :(") },
            confirmButton = {},
            dismissButton = {}
        )
        Log.d("ConnectionEvent", "Houston, we lost connectivity")
    }

    DisposableEffect(key1 = connectivityReceiver) {
        onDispose {
            connectivityReceiver.unregister()
        }
    }


}

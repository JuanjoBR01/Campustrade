package com.example.campustrade

import androidx.compose.foundation.Image
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.campustrade.ui.theme.CampustradeTheme
import com.example.campustrade.ui.theme.darkBlue
import com.example.campustrade.ui.theme.orange
import com.example.campustrade.ui.theme.yellow


class LoginScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampustradeTheme{
                LoginScreenComposable(viewModel = LoginViewModel(LoginRepository()))
            }
        }
    }
}



@Composable
fun LoginScreenComposable(modifier: Modifier = Modifier, viewModel: LoginViewModel) {

    val emailField: String by viewModel.email.observeAsState(initial = "")
    val passwordField: String by viewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)


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
                var aux = viewModel.onLoginSelected(emailField, passwordField)
                if (aux) {
                    Toast.makeText(
                        context,
                        "Login successful",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(context, HomeActivityMVVM::class.java)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(
                        context,
                        "There was a problem logging you in.",
                        Toast.LENGTH_LONG
                    ).show()
                }
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
            )

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

    }

}


@Preview
@Composable
fun LoginScreenPreview() {
    CampustradeTheme {
        LoginScreenComposable(viewModel = LoginViewModel(LoginRepository()))
    }
}

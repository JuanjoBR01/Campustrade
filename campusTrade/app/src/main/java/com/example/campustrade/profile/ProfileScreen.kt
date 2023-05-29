package com.example.campustrade.profile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.tooling.preview.Preview
import com.example.campustrade.ui.theme.CampustradeTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.campustrade.R
import com.example.campustrade.ui.theme.white
import com.example.campustrade.home.HomeActivityMVVM
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.draw.rotate
import coil.compose.rememberImagePainter
import com.example.campustrade.objects.CurrentUser
import com.example.campustrade.ui.theme.orange

import androidx.compose.runtime.Composable



class ProfileScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CampustradeTheme{
                ProfileScreenComposable()
            }
        }
    }
}


@Composable
fun ProfileScreenComposable(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("logged_user_preferences", Context.MODE_PRIVATE)

    val nameField = sharedPreferences.getString("name", "     ")
    val emailField = sharedPreferences.getString("email", "     ")
    val imageUrl = sharedPreferences.getString("image", "     ")
    val tagField = sharedPreferences.getString("tag", "     ")


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
    ) {
        TopAppBar(
            title = { Text(
                stringResource(R.string.profile_top_bar_text),
                style = MaterialTheme.typography.h2) },
            modifier = modifier,
            navigationIcon = {
                IconButton(
                    onClick = { val intent = Intent(context, HomeActivityMVVM::class.java)
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

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = modifier
                .fillMaxWidth()
                .align(Start)
                .padding(start = 10.dp, end = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFFFFFFF))
                    .height(120.dp)
                    .width(120.dp)
            ) {

                Image(
                    painter = rememberImagePainter(
                        data = imageUrl,
                        builder = {
                            crossfade(true)
                            placeholder(R.drawable.programmer)
                            error(R.drawable.sampleuser)
                        }
                    ),
                    contentDescription = "Imagen",
                    modifier = Modifier.rotate(90f).height(120.dp)
                )






            }

            Column (
                modifier = modifier
                    .fillMaxWidth()
                    .align(CenterVertically)
                ){
                Text(text = if (nameField!!.length > 20) nameField.substring(0,17) + "..." else nameField,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.align(CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = if (emailField!!.substring(0,emailField.indexOf("@")).length > 20) emailField.substring(0,17) + "..." else emailField.substring(0,emailField.indexOf("@")),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.align(CenterHorizontally)
                )

            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Divider(color = Color.LightGray,
            thickness = 1.dp,
        )

        Spacer(modifier = Modifier.height(15.dp))

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
        ) {

            Text(text = "Settings",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.align(Start)
            )

            Spacer(modifier = Modifier.height(15.dp))


            Row(
                modifier = Modifier
                    .height(30.dp)
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Notifications",
                    style = MaterialTheme.typography.h5,
                    modifier = modifier.weight(1f)
                )

                Image(
                    painter = painterResource(id = R.drawable.baseline_chevron_right_black_48),
                    contentDescription = null,
                    modifier = modifier
                        .height(30.dp)
                        .width(30.dp)
                        .background(color = Color.Transparent),
                )


            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .height(30.dp)
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Security options",
                    style = MaterialTheme.typography.h5,
                    modifier = modifier.weight(1f)
                )

                Image(
                    painter = painterResource(id = R.drawable.baseline_chevron_right_black_48),
                    contentDescription = null,
                    modifier = modifier
                        .height(30.dp)
                        .width(30.dp)
                        .background(color = Color.Transparent),
                )


            }

            Spacer(modifier = Modifier.height(15.dp))


            Row(
                modifier = Modifier
                    .height(30.dp)
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "About Campustrade",
                    style = MaterialTheme.typography.h5,
                    modifier = modifier.weight(1f)
                )

                Image(
                    painter = painterResource(id = R.drawable.baseline_chevron_right_black_48),
                    contentDescription = null,
                    modifier = modifier
                        .height(30.dp)
                        .width(30.dp)
                        .background(color = Color.Transparent),
                )


            }


        }

        Spacer(modifier = Modifier.height(10.dp))


        Divider(color = Color.LightGray,
            thickness = 1.dp,
        )

        Spacer(modifier = Modifier.height(15.dp))

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
        ) {

            Text(text = "About you",
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .align(Start)
                    .padding(start = 5.dp, end = 10.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row {
                Text(text = "Your tag:  ",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(start = 25.dp, end = 10.dp)
                )

                Text(text = tagField!!,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(start = 25.dp, end = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row {
                Text(text = "Last login:  ",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(start = 25.dp, end = 10.dp)
                )

                Text(text = CurrentUser.user!!.lastaccess,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(start = 25.dp, end = 10.dp)
                )
            }


            Spacer(modifier = Modifier.height(30.dp))


            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = orange,
                    disabledBackgroundColor = Color(0xFF535D64)
                ),
                modifier = Modifier
                    .width(200.dp)
                    .align(CenterHorizontally),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 0.dp
                )

            ) {
                Text(stringResource(id = R.string.log_out_button),
                    color = MaterialTheme.colors.background)
            }

        }


    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    CampustradeTheme {
        ProfileScreenComposable()
    }
}
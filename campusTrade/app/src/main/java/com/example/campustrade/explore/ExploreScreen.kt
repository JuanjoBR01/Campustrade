package com.example.campustrade.explore

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.campustrade.ConnectivityReceiver
import com.example.campustrade.R
import com.example.campustrade.data.Resource
import com.example.campustrade.home.HomeActivityMVVM
import com.example.campustrade.objects.Coordinates
import com.example.campustrade.prodsProfile.ProdsPScreenActivity
import com.example.campustrade.ui.theme.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


class ExploreScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CampustradeTheme{
                ExploreScreenComposable(viewModel = ExploreViewModel())
            }
        }
    }
}


@Composable
@OptIn(ExperimentalPermissionsApi::class)

fun ExploreScreenComposable(modifier: Modifier = Modifier, viewModel: ExploreViewModel) {

    val context = LocalContext.current

    val mapButtonEnabled: Boolean by viewModel.mapButtonEnabled.observeAsState(initial = true)
    val loginFlow = viewModel.loginFlow.collectAsState()

    val connectivityReceiver = remember { ConnectivityReceiver(context = context) }
    connectivityReceiver.register()


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
    ) {
        TopAppBar(
            title = {
                Text(
                    stringResource(R.string.explore_top_bar_text),
                    style = MaterialTheme.typography.h2
                )
            },
            modifier = modifier,
            navigationIcon = {
                IconButton(
                    onClick = {
                        val intent = Intent(context, HomeActivityMVVM::class.java)
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

        Spacer(modifier = Modifier.height(15.dp))

        Column(modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)) {
            Text(text = "Explore Nearby Distributors",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(10.dp))

            AccessFinePermission()

            Spacer(modifier = Modifier.height(15.dp))

            Image(
                painter = painterResource(id = R.drawable.mapv2),
                contentDescription = null,
                modifier = modifier
                    .width(300.dp)
                    .height(300.dp)
                    .background(color = Color.Transparent)
                    .clip(CircleShape)
                    .align(CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(25.dp))

            Column (modifier = Modifier.align(CenterHorizontally)){
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = orange),
                    onClick = {
                        if (connectivityReceiver.isOnline) {
                            viewModel.getDistributors(context)
                        } else if (Coordinates.distributors.size > 1) {
                            val intent = Intent(context, MapScreen::class.java)
                            context.startActivity(intent)
                        }

                    },
                    enabled = mapButtonEnabled
                ) {
                    Text(text = "EXPLORE MAP")
                }

                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = orange),
                    onClick = {
                        val intent = Intent(context, ProdsPScreenActivity::class.java)
                        context.startActivity(intent)
                    },
                    enabled = mapButtonEnabled
                ) {
                    Text(text = "EXPLORE SELLERS")
                }

            }
        }

        NoInternetText(context = context)


        loginFlow.value?.let {
            when(it) {
                is Resource.Failure ->{
                    val context = LocalContext.current
                    Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    CircularProgressIndicator(Modifier.align(CenterHorizontally))

                }
                is Resource.Success -> {
                    LaunchedEffect(Unit) {
                        viewModel.restartState()
                        val intent = Intent(context, MapScreen::class.java)
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
@ExperimentalPermissionsApi
private fun AccessFinePermission() {

    Column(Modifier.fillMaxWidth(),verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        // Fine Location permission state
        val fineLocationPermissionState = rememberPermissionState(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        GetFineLocationPermission(perm = fineLocationPermissionState)
    }
}

@ExperimentalPermissionsApi
@Composable
private fun GetFineLocationPermission(
    perm: PermissionState
) {
    if(perm.hasPermission){
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally) {

            Text(
                "Find distributors around you and their speciality!",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(5.dp))
        }
    }
    else{
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally) {

            Text(
                "In order to access to this feature, please accept this permission",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { perm.launchPermissionRequest()},
                colors = ButtonDefaults.buttonColors(backgroundColor = yellow),
                modifier = Modifier
                    .width(300.dp)
                    .height(40.dp)) {
                Text("REQUEST LOCATION PERMISSION", color = darkBlue,
                    style = MaterialTheme.typography.button
                )
            }


        }
    }
}


@Preview
@Composable
fun ExploreScreenPreview() {
    CampustradeTheme {
        ExploreScreenComposable(viewModel = ExploreViewModel())
    }
}

@Composable
fun NoInternetText(context: Context) {
    val connectivityReceiver = remember { ConnectivityReceiver(context = context) }
    connectivityReceiver.register()

    if (!connectivityReceiver.isOnline) {
        Text(text = "Disconnected! If you press the map button and nothing is cached, the button won't work :(",
            color = Color.Red,
            style = MaterialTheme.typography.body2,

            )
        Spacer(modifier = Modifier.height(24.dp))

        Log.d("ConnectionEvent", "Houston, we lost connectivity")
    }

    DisposableEffect(key1 = connectivityReceiver) {
        onDispose {
            connectivityReceiver.unregister()
        }
    }
}

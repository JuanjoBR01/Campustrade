package com.example.campustrade.explore

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
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
import com.example.campustrade.R
import com.example.campustrade.home.HomeActivityMVVM
import com.example.campustrade.ui.theme.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices


class ExploreScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CampustradeTheme{
                ExploreScreenComposable()
            }
        }
    }
}


@Composable
@OptIn(ExperimentalPermissionsApi::class)

fun ExploreScreenComposable(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    var locationText by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var neighbourhood by remember { mutableStateOf("") }
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)
    val neighborhoodName = remember { mutableStateOf("") }


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
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {

                        }
                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            location?.let {
                                val latitude = location.latitude
                                val longitude = location.longitude
                                locationText = "Latitud: $latitude\nLongitud: $longitude"

                                city = getCityName(latitude, longitude, context)
                                neighbourhood = getNeighbourhoodName(latitude, longitude, context)
                            }
                        }
                    }
                ) {
                    Text(text = "EXPLORE MAP")
                }

                /*
                Text(text = locationText)
                Text(text = city)
                Text(text = neighbourhood)
                 */




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


fun getCityName(latitude: Double, longitude: Double, context: Context): String {
    var cityName = ""
    val geocoder = Geocoder(context)


        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                cityName = addresses[0].locality ?: ""
            }
        }

    return cityName
}

fun getNeighbourhoodName(latitude: Double, longitude: Double, context: Context): String {
    var neighbourhoodName = ""
    val geocoder = Geocoder(context)


    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
    if (addresses != null) {
        if (addresses.isNotEmpty()) {
            neighbourhoodName = addresses[0].subLocality ?: ""
        }
    }

    return neighbourhoodName
}

@Preview
@Composable
fun ExploreScreenPreview() {
    CampustradeTheme {
        ExploreScreenComposable()
    }
}

package com.example.campustrade.explore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campustrade.R
import com.example.campustrade.objects.Coordinates
import com.example.campustrade.ui.theme.CampustradeTheme
import com.example.campustrade.ui.theme.white
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState


class MapScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CampustradeTheme{
                MapScreenComposable(viewModel = MapViewModel())
            }
        }
    }
}


@Composable
fun MapScreenComposable(modifier: Modifier = Modifier, viewModel: MapViewModel) {

    val distributors = Coordinates.distributors
    var latlng: LatLng

    val nearbyDistributors: Int by viewModel.nearbyDistributors.observeAsState(initial = 0)
    val materials: Int by viewModel.materials.observeAsState(initial = 0)
    val accessories: Int by viewModel.accessories.observeAsState(initial = 0)
    val products: Int by viewModel.products.observeAsState(initial = 0)
    val others: Int by viewModel.others.observeAsState(initial = 0)



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
            modifier = modifier.align(CenterHorizontally),
            backgroundColor = white,
            contentColor = Color.Black,
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            "There are $nearbyDistributors distributors close to you!",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            "Their specialization is distributed as follows:",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            "$materials in materials   \t   $products in products \n $accessories in accessories   \t   $others in others.",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.align(CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Divider(color = Color.LightGray, thickness = 1.dp)

        Spacer(modifier = Modifier.height(10.dp))


        GoogleMap(modifier = Modifier.fillMaxSize(),
            cameraPositionState = CameraPositionState(
                position = CameraPosition(
                    Coordinates.userCoordinates,
                    15f, 0f,0f))
        ) {
            Marker(position = Coordinates.userCoordinates,
                title = "You are here!",
                snippet = "${Coordinates.userNeighbourhood}, ${Coordinates.userCity}",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            for (distributor in distributors) {
                latlng = LatLng(distributor.latitude,distributor.longitude)
                Marker(position = latlng, title = distributor.name, snippet = distributor.tag)
            }
        }




    }
}

@Preview
@Composable
fun MapScreenPreview() {
    CampustradeTheme {
        MapScreenComposable(viewModel = MapViewModel())
    }
}

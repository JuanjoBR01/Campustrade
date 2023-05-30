package com.example.campustrade.explore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campustrade.ui.theme.CampustradeTheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.android.gms.maps.model.LatLng


class MapScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CampustradeTheme{
                MapScreenComposable()
            }
        }
    }
}


@Composable
fun MapScreenComposable(modifier: Modifier = Modifier) {

    val markers: List<LatLng> = listOf(LatLng(4.60330076339536,-74.0671900411582),
        LatLng(4.60003278801175,-74.0624545802107),
        LatLng(4.602848787576,-74.0685299278975),
        LatLng(4.62857804268645,-74.0645638572091),
        LatLng(4.65678138370675,-74.0562320044848),
        LatLng(4.60022861157303,-74.0733619663251),
        LatLng(4.8600019981799,-74.0329896330222),
        LatLng(6.19979470650579,-75.579278860776),
        LatLng(7.13170273926876,-73.1128293229071),
        LatLng(11.0222580669123,-74.8499203151008)
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
    ) {

        GoogleMap(modifier = Modifier.fillMaxSize()) {
            for (marker in markers) {
                Marker(position = marker)
            }
        }
    }
}

@Preview
@Composable
fun MapScreenPreview() {
    CampustradeTheme {
        MapScreenComposable()
    }
}

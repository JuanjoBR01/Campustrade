package com.example.campustrade.objects

import com.example.campustrade.dtos.Distributor
import com.google.android.gms.maps.model.LatLng

object Coordinates {
    val userCoordinates: LatLng = LatLng(4.72999875725272, -74.0361344658211)
    val distributorsCoordinates: List<Distributor> = listOf(Distributor("Lego",
        "Material",
        4.72999875725272,
        -74.0361344658211))
}
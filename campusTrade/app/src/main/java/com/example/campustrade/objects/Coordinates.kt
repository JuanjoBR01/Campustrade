package com.example.campustrade.objects

import com.example.campustrade.dtos.Distributor
import com.google.android.gms.maps.model.LatLng

object Coordinates {
    var userCity: String = "Athens"
    var userNeighbourhood: String = "Cuadra Picha"
    var userCoordinates: LatLng = LatLng(4.72999875725272, -74.0361344658211)
    var distributors: List<Distributor> = listOf(Distributor("Lego",
        "Material",
        4.72999875725272,
        -74.0361344658211,
        "Athens"))
}
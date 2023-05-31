package com.example.campustrade.dtos

data class Distributor(
    val name: String,
    val tag: String,
    val latitude: Double,
    val longitude: Double,
    val city: String
) {
    constructor(): this("","",4.601748588265035, -74.06621857886229, "")
}

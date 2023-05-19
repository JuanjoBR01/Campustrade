package com.example.campustrade.dtos

data class PurchaseInfo(val numPurchases: Int, val totalPurchased: Int, val typePurchased: String, val condPurchased: String){
    constructor(): this(0, 0, "Accesory","Used")
}
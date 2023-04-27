package com.example.campustrade.repository

import android.graphics.Bitmap
import com.example.campustrade.ProductDB
import com.example.campustrade.dtos.ProductObj

interface RepositoryInterface{
    suspend fun getData(): List<ProductDB>
    suspend fun makeLogin(email: String,password: String): Boolean
    suspend fun uploadImageToDataBase(productOb: ProductObj, bitmp: Bitmap): ProductObj
    suspend fun uploadProductToDB(productOb: ProductObj):Boolean
}
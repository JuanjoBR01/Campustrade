package com.example.campustrade.repository

import android.graphics.Bitmap
import com.example.campustrade.ProductDB
import com.example.campustrade.dtos.ProductObj
import com.example.campustrade.dtos.UsersProdsObj

interface RepositoryInterface{
    suspend fun getData(): List<ProductDB>

    suspend fun getMyObjects(): List<UsersProdsObj>
    suspend fun makeLogin(email: String,password: String): Boolean
    suspend fun uploadImageToDataBase(productOb: ProductObj, bitmp: Bitmap): ProductObj
    suspend fun uploadProductToDB(productOb: ProductObj):Boolean
    suspend fun createUser(vt: String, nn: String, em: String, pw: String, imgUrl: String): Boolean
}
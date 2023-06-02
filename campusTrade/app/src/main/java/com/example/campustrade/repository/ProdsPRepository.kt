package com.example.campustrade.repository

import android.graphics.Bitmap
import com.example.campustrade.ProductDB
import com.example.campustrade.dtos.ProductObj

class ProdsPRepository : RepositoryInterface {
    override suspend fun getData(): List<ProductDB> {
        TODO("Not yet implemented")
    }

    override suspend fun makeLogin(email: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImageToDataBase(productOb: ProductObj, bitmp: Bitmap): ProductObj {
        TODO("Not yet implemented")
    }

    override suspend fun uploadProductToDB(productOb: ProductObj): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(
        vt: String,
        nn: String,
        em: String,
        pw: String,
        imgUrl: String
    ): Boolean {
        TODO("Not yet implemented")
    }


}
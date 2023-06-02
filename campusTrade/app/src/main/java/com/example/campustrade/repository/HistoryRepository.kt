package com.example.campustrade.repository

import android.graphics.Bitmap
import com.example.campustrade.home.HomeRepositoryInterface
import com.example.campustrade.ProductDB
import com.example.campustrade.dtos.ProductObj
import com.example.campustrade.dtos.UsersProdsObj
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class HistoryRepository:  RepositoryInterface  {
    override suspend fun getData(): List<ProductDB> {
        //var listResp : List<ProductDB> = emptyList()

        // on below line creating an instance of firebase firestore.
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val collec = db.collection("ProductsDB")

        val querySnapshot = collec.get().await()

        return querySnapshot.toObjects<ProductDB>()
    }

    override suspend fun getMyObjects(): List<UsersProdsObj> {
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
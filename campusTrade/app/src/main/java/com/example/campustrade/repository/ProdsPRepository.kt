package com.example.campustrade.repository

import android.graphics.Bitmap
import com.example.campustrade.ProductDB
import com.example.campustrade.data.utils.await
import com.example.campustrade.dtos.ProductObj
import com.example.campustrade.dtos.UsersProdsObj
import com.google.firebase.firestore.FirebaseFirestore

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

    override suspend fun getMyObjects(): List<UsersProdsObj> {
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("users")

        val snapshot = collectionRef.get().await()
        val myObjectsList = mutableListOf<UsersProdsObj>()

        for (document in snapshot.documents) {
            val myObject = document.toObject(UsersProdsObj::class.java)
            myObject?.let {
                myObjectsList.add(it)
            }
        }

        return myObjectsList
    }

}
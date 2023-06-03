package com.example.campustrade.product

import com.example.campustrade.ProductDB
import com.example.campustrade.home.HomeRepositoryInterface
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class ProductRepository: HomeRepositoryInterface {

    override suspend fun getData(): List<ProductDB> {

        //var listResp : List<ProductDB> = emptyList()

        // on below line creating an instance of firebase fireStore.
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val collec = db.collection("ProductsDB")

        val querySnapshot = collec.get().await()

        return querySnapshot.toObjects<ProductDB>()
    }
}
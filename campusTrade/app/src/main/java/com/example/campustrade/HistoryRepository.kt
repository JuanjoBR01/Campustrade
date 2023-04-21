package com.example.campustrade

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class HistoryRepository: HomeRepositoryInterface {
    override suspend fun getData(): List<ProductDB> {
        //var listResp : List<ProductDB> = emptyList()

        // on below line creating an instance of firebase firestore.
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val collec = db.collection("ProductsDB")

        val querySnapshot = collec.get().await()

        return querySnapshot.toObjects<ProductDB>()
    }
}
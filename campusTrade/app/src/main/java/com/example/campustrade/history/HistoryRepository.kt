package com.example.campustrade.history

import com.example.campustrade.home.HomeRepositoryInterface
import com.example.campustrade.ProductDB
import com.example.campustrade.dtos.PurchaseInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class HistoryRepository: HomeRepositoryInterface {
    override suspend fun getData(): List<ProductDB> {
        //var listResp : List<ProductDB> = emptyList()

        // on below line creating an instance of firebase firestore.
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val collec = db.collection("UserProductDB")

        val querySnapshot = collec.get().await()

        return querySnapshot.toObjects<ProductDB>()
    }

    suspend fun getPurchaseData(): List<PurchaseInfo>? {

        try {
            //var listResp : List<ProductDB> = emptyList()

            // on below line creating an instance of firebase firestore.
            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
            val collec = db.collection("PurchaseInfoDB")
            val querySnapshot = collec.get().await()
            return querySnapshot.toObjects<PurchaseInfo>()
        }
        catch(e: Exception)
        {
            return null
        }
    }
}
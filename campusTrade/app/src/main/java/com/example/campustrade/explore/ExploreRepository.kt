package com.example.campustrade.explore

import com.example.campustrade.dtos.Distributor
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class ExploreRepository {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getDistributors(): List<Distributor> {
        val collec = db.collection("Distributors")
        val querySnapshot = collec.get().await()
        return querySnapshot.toObjects<Distributor>()
    }

}
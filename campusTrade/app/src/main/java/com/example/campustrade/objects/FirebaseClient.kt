package com.example.campustrade.objects

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseClient {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
}


/*
@Singleton
class FirebaseClient @Inject constructor() {
    val auth: FirebaseAuth get() = FirebaseAuth.getInstance()
    val fireStore: FirebaseFireStore get() = FirebaseFireStore.getInstance()
}*/
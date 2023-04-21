package com.example.campustrade

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseClient @Inject constructor() {
    val auth: FirebaseAuth get() = FirebaseAuth.getInstance()
    val fireStore: FirebaseFirestore get() = FirebaseFirestore.getInstance()
}
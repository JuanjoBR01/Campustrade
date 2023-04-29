package com.example.campustrade.signup

import com.example.campustrade.data.Resource
import com.example.campustrade.data.utils.await
import com.example.campustrade.objects.FirebaseClient
import com.example.campustrade.dtos.UserObj
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class SignUpRepository @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore
): SignUpRepositoryInterface  {

    override suspend fun createUser(vt: String, nn: String, em: String, pw: String, imgUrl: String): Unit {
        val dbUsers: CollectionReference = firebaseFireStore.collection("users")
        val userObj = UserObj(nn, em, vt,imgUrl)
        dbUsers.add(userObj).addOnSuccessListener {
        }.addOnFailureListener {
        }
    }

}
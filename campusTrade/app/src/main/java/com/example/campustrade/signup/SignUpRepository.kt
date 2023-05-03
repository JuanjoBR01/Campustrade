package com.example.campustrade.signup

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.campustrade.dtos.UserObj
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import javax.inject.Inject

class SignUpRepository @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore
) : SignUpRepositoryInterface {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createUser(
        vt: String,
        nn: String,
        em: String,
        pw: String,
        imgUrl: String
    ) {
        val dbUsers: CollectionReference = firebaseFireStore.collection("users")
        val accessDate = LocalDateTime.now()
        val accessString = "${accessDate.dayOfMonth}/${accessDate.monthValue}/${accessDate.year} - ${accessDate.hour}:${accessDate.minute}"
        val userObj = UserObj(nn, em, vt, imgUrl, accessString)
        dbUsers.add(userObj).addOnSuccessListener {
        }.addOnFailureListener {
        }
    }
}
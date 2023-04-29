package com.example.campustrade.signup

import com.example.campustrade.data.Resource
import com.google.firebase.auth.FirebaseUser

interface SignUpRepositoryInterface {
    suspend fun createUser(vt: String, nn: String, em: String, pw: String, imgUrl: String): Unit
}
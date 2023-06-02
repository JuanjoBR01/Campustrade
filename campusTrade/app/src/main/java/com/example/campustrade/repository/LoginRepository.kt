package com.example.campustrade.repository

import android.graphics.Bitmap
import android.util.Log
import com.example.campustrade.FirebaseClient
import com.example.campustrade.ProductDB
import com.example.campustrade.dtos.ProductObj
import com.example.campustrade.dtos.UsersProdsObj

class LoginRepository: RepositoryInterface {

    private val firebaseAuth = FirebaseClient().auth
    override suspend fun getData(): List<ProductDB> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyObjects(): List<UsersProdsObj> {
        TODO("Not yet implemented")
    }

    override suspend fun makeLogin(email: String, password: String): Boolean {
        var aux = true
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    aux = true
                    Log.d("TAG", "User logged successfully")
                } else {
                    //_signInResult.value = Result.failure(task.exception ?: Exception("Unknown error occurred")
                    aux = false
                    Log.d("TAG", "User couldn't log")
                }
            }
        return aux
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


}
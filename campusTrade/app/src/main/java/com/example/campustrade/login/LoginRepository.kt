package com.example.campustrade.login


import android.util.Log
import com.example.campustrade.FirebaseClient

class LoginRepository: LoginRepositoryInterface {

    private val firebaseAuth = FirebaseClient().auth
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


}
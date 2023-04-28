package com.example.campustrade.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.campustrade.objects.FirebaseClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepository() {

    private val firebaseAuth = FirebaseClient.auth

    private val _successfulLogin = MutableLiveData<Boolean>()
    val successfulLogin: LiveData<Boolean> = _successfulLogin

    fun makeLogin(email: String, password: String) {
        //var aux = true

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _successfulLogin.value = task.isSuccessful
                print(_successfulLogin.value)
                /*
                if (task.isSuccessful) {
                    // aux = true
                    Log.d("TAG", "User logged successfully")
                } else {
                    //_signInResult.value = Result.failure(task.exception ?: Exception("Unknown error occurred")
                    // aux = false
                    Log.d("TAG", "User couldn't log")
                }*/
            }


        // return aux

    }


}
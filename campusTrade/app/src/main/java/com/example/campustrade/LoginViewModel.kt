package com.example.campustrade

import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.campustrade.ui.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.update



class LoginViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private lateinit var currentUser: String

    var userMail by mutableStateOf("")
        private set

    var userPassword by mutableStateOf("")
        private set

    private val maxLen = 31

    fun resetView () {
        _uiState.value = LoginUiState("", "")
    }

    init {
        resetView()
    }


    fun updateUserMail(emailField: String){
        if (emailField.length < maxLen){
            userMail = emailField
        }
    }

    fun updateUserPassword(passwordField: String){
        if (passwordField.length < maxLen){
            userPassword = passwordField
        }
    }

    fun sendData (): String {
        var txState: String = "defaultValue"
        if (userMail != "" && userPassword != "") {
            FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(userMail, userPassword)
                .addOnCompleteListener() {
                    txState = if (it.isSuccessful) {
                        "Login Successful"
                    } else {
                        "It was a problem logging you in"
                    }
                }
        } else {
            txState = "The credentials are invalid"
        }
        return txState
    }

}

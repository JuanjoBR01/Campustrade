package com.example.campustrade.login

import android.content.Context
import android.net.*
import android.os.Build
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.campustrade.data.Resource
import com.example.campustrade.repository.AuthRepository
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
):
    ViewModel(){

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _signupEnable = MutableLiveData<Boolean>(true)
    val signupEnable: LiveData<Boolean> = _signupEnable

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _networkState = MutableLiveData<Boolean>(true)
    val networkState: LiveData<Boolean> = _networkState

    private val _connectDialg = MutableLiveData<Boolean>(false)
    val connectDialg: LiveData<Boolean> = _connectDialg

    private val _message = MutableLiveData<String>("")
    val message: LiveData<String> = _message

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    fun onLoginChanged(email: String, password: String){
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)
        _loginFlow.value = Resource.PastFailure
    }

    fun changeButtonState(state: Boolean) {
        _loginEnable.value = state
    }

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword(password: String): Boolean = password.length > 6

    init {
        logOut()
        if(repository.currentUser != null) {
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        _signupEnable.value = false
        _loginEnable.value = false
        val result = repository.login(email, password)
        _loginFlow.value = result
        _loginEnable.value = true
        _signupEnable.value = true

    }

    fun onNetworkStateChanged(pIsConnected: Boolean) {
        _networkState.postValue(pIsConnected)
        if (!pIsConnected) {
            _message.postValue("No internet connection, connect and try again.")
            _connectDialg.postValue(true)
        } else {
            _message.postValue("Connected.")
            _connectDialg.postValue(false)

        }
    }

    fun logOut() {
        repository.logout()
        _loginFlow.value = null
    }


}
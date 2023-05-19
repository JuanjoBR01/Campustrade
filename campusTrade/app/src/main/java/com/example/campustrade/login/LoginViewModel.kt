package com.example.campustrade.login

import android.content.Context
import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
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
import com.example.campustrade.profile.UsersRepository
import com.example.campustrade.repository.AuthRepository
import com.example.campustrade.repository.TelemetryRepository
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
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

    private val _signupEnable = MutableLiveData(true)
    val signupEnable: LiveData<Boolean> = _signupEnable

    private val _launchedTime = MutableLiveData(false)
    val launchedTime: LiveData<Boolean> = _launchedTime

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    private val usersRepository = UsersRepository()

    private val telemetryRepository = TelemetryRepository()

    fun onLoginChanged(email: String, password: String){
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)
        _loginFlow.value = Resource.PastFailure
    }

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword(password: String): Boolean = password.length > 6

    init {
        logOut()
        if(repository.currentUser != null) {
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }



    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun login(email: String, password: String, context: Context) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        _signupEnable.value = false
        _loginEnable.value = false
        val result = repository.login(email, password)

        val accessDate = LocalDateTime.now()
        val accessString = "${accessDate.dayOfMonth}/${accessDate.monthValue}/${accessDate.year} - ${if(accessDate.hour<10) "0" + accessDate.hour else accessDate.hour}:${if(accessDate.minute<10) "0" + accessDate.minute else accessDate.minute}"

        usersRepository.updateDate(email, accessString, context)


        _loginFlow.value = result
        _loginEnable.value = true
        _signupEnable.value = true

    }


    fun logOut() {
        repository.logout()
        _loginFlow.value = null
    }

    fun uploadLaunchTime() =  viewModelScope.launch{
        telemetryRepository.uploadLaunchTime()
    }

    fun launched() {
        _launchedTime.value = true
    }





}
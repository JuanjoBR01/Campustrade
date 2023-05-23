package com.example.campustrade.signup

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campustrade.data.Resource
import com.example.campustrade.objects.FirebaseClient
import com.example.campustrade.objects.SuHashMap.suMap
import com.example.campustrade.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
    ):
    ViewModel() {

    private val _expanded = MutableLiveData<Boolean>()
    val expanded: LiveData<Boolean> = _expanded

    private val _valueType = MutableLiveData<String>()
    val valueType: LiveData<String> = _valueType

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: LiveData<String> = _confirmPassword

    private val _signUpFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signUpFlow: StateFlow<Resource<FirebaseUser>?> = _signUpFlow

    private val creationRepository = SignUpRepository(FirebaseClient.fireStore)




    fun onExpandedChange() {
        _expanded.value = _expanded.value != true
    }

    fun setExpandedFalse() {
        _expanded.value = false
    }

    fun onValueTypeChange(vt: String) {
        _valueType.value = vt
    }

    fun onNameChange(nn: String) {
        _name.value = nn
        suMap["nn"] = nn
    }

    fun onEmailChange(em: String) {
        _email.value = em
        suMap["em"] = em
    }

    fun onPasswordChange(pw: String) {
        _password.value = pw
    }

    fun onConfirmPasswordChange(cpw: String) {
        _confirmPassword.value = cpw
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createUser(vt: String, nn: String, em: String, pw: String, imgUrl: String) = viewModelScope.launch (Dispatchers.IO){
        creationRepository.createUser(vt, nn, em, pw, imgUrl)
    }

    init {
        if (suMap["nn"] == null) {
            suMap["nn"] = ""
        }
        if (suMap["em"] == null) {
            suMap["em"] = ""
        }

        _name.value = suMap["nn"]
        _email.value = suMap["em"]

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadImage(context: Context, contentImage: Uri?, vt: String, nn: String, em: String, pw: String) = viewModelScope.launch {
        _signUpFlow.value = Resource.Loading

        withContext(Dispatchers.IO) {
            val result = repository.signup(nn, em, pw)
            repository.login(em, pw)
            _signUpFlow.value = result

            val storageRef = Firebase.storage.reference
            //Transform to bitmap
            val inputStream = context.contentResolver.openInputStream(contentImage!!)
            val bitmp: Bitmap = BitmapFactory.decodeStream(inputStream)
            //Bitmap to bytes
            val outputStream = ByteArrayOutputStream()
            bitmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val bytes = outputStream.toByteArray()
            //Create variable to store url
            var imgUrl: String
            //Upload to DB
            val storeR = storageRef.child("images/${UUID.randomUUID()}")
            val uploadTask = storeR.putBytes(bytes)
            uploadTask.addOnSuccessListener {
                storeR.downloadUrl.addOnSuccessListener { uri ->
                    imgUrl = uri.toString()
                    createUser(vt, nn, em, pw, imgUrl)

                }
            }
        }
    }

    fun restartForm() {
        _password.value = ""
        _confirmPassword.value = ""
    }
}

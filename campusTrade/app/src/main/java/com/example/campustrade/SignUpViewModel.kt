package com.example.campustrade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SignUpViewModel(private val repository: SignUpRepository): ViewModel() {

    private val _expanded = MutableLiveData<Boolean>()
    val expanded: LiveData<Boolean> = _expanded

    //private val _mTextFieldSize = MutableLiveData<Size>()
    //val mTextFieldSize: LiveData<Size> = _mTextFieldSize

    val prodType = arrayOf("Material", "Product", "Accessory", "Other")

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
    }

    fun onEmailChange(em: String) {
        _email.value = em
    }

    fun onPasswordChange(pw: String) {
        _password.value = pw
    }

    fun onConfirmPasswordChange(cpw: String) {
        _confirmPassword.value = cpw
    }

    private val firebaseAuth = FirebaseClient().auth
    private val firebaseFireStore = FirebaseClient().fireStore

    fun createUser(vt: String, nn: String, em: String, pw: String): Boolean {
        var aux = true
        runBlocking {
            launch {
                aux = repository.createUser(vt, nn, em, pw)
            }
        }

        return aux

    }





}

package com.example.campustrade

import android.widget.Toast
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class SignUpViewModel: ViewModel() {

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

    fun createUser(vt: String, nn: String, em: String, pw: String): Boolean {
        var aux = true
        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(em, pw)
            .addOnCompleteListener(){
                if (it.isSuccessful) {

                    // on below line creating an instance of firebase firesStore.
                    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                    //creating a collection reference for our Firebase FireStore database.
                    val dbUsers: CollectionReference = db.collection("users")
                    //adding our data to our courses object class.
                    val userObj = UserObj(nn, em, vt,"https://firebasestorage.googleapis.com/v0/b/campustrade-6d7b6.appspot.com/o/images%2Fowl.jpg?alt=media&token=d9dd4852-dcad-4811-9739-36909d731a6d")
                    //below method is use to add data to Firebase FireStore.
                    dbUsers.add(userObj).addOnSuccessListener {
                        // after the data addition is successful
                        // we are displaying a success toast message.
                    }.addOnFailureListener { e ->
                        // this method is called when the data addition process is failed.
                        // displaying a toast message when data addition is failed.
                        aux = false
                    }
                } else {
                    aux = false
                }
            }

        return aux

    }





}

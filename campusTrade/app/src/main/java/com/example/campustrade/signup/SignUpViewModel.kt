package com.example.campustrade.signup

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.util.*


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

    fun createUser(vt: String, nn: String, em: String, pw: String, imgUrl: String): Boolean {
        var aux = true
        runBlocking {
            launch {
                aux = repository.createUser(vt, nn, em, pw, imgUrl)
            }
        }

        return aux
    }

    fun uploadImage(context: Context, contentImage: Uri?, viewModel: SignUpViewModel, vt: String, nn: String, em: String, pw: String): Boolean {
        val storageRef = Firebase.storage.reference

        //Transform to bitmap
        val inputStream = context.contentResolver.openInputStream(contentImage!!)
        val bitmp: Bitmap = BitmapFactory.decodeStream(inputStream)

        //Bitmap to bytes
        val outputStream = ByteArrayOutputStream()
        bitmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val bytes = outputStream.toByteArray()

        //Create variable to store url
        var imgUrl = ""
        var aux = true

        //Upload to DB
        val storeR = storageRef.child("images/${UUID.randomUUID()}")
        val uploadTask = storeR.putBytes(bytes)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            storeR.downloadUrl.addOnSuccessListener { uri ->
                imgUrl = uri.toString()

                aux = viewModel.createUser(vt, nn, em, pw, imgUrl)
            }
        }
        return aux
    }

    fun restartForm() {
        _valueType.value = ""
        _name.value = ""
        _email.value = ""
        _password.value = ""
        _confirmPassword.value = ""
    }
}

package com.example.campustrade.prodsProfile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campustrade.repository.RepositoryFactory
import com.example.campustrade.repository.RepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProdsPViewModel() : ViewModel() {

    //Conection to DB
    private val repository: RepositoryInterface = RepositoryFactory.createRepository("ProdsP")

    //Atributo del nombre del usuario
    private val _userName = MutableLiveData<String>("Juan Prueba")
    val userName: LiveData<String> = _userName

    //Atributo del image
    private val _prodImage = MutableLiveData<Uri?>()
    val prodImage: LiveData<Uri?> = _prodImage

    //Atributo del image
    private val _prodName = MutableLiveData<String>("")
    val prodName: LiveData<String> = _prodName

    //Atributo del precio del product
    private val _prodPrice = MutableLiveData<String>("")
    val prodPrice: LiveData<String> = _prodPrice

    //Atributo de los tags
    private val _prodTag = MutableLiveData<String>("")
    val prodTag: LiveData<String> = _prodTag

    //Lista de usuarios
    private val _arrayName = MutableLiveData<Array<String>>()
    val arrayName: LiveData<Array<String>> = _arrayName


    fun onNetworkStateChanged(connection:Boolean){

    }

    fun fetchMyObjects() {
        val scope = CoroutineScope(Dispatchers.IO)
            scope.launch(Dispatchers.IO) {
                val myObjects = repository.getMyObjects()
            }
    }






}
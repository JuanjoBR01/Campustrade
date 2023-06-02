package com.example.campustrade.prodsProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campustrade.repository.RepositoryFactory
import com.example.campustrade.repository.RepositoryInterface

class ProdsPViewModel() : ViewModel() {

    //Conection to DB
    private val repository: RepositoryInterface = RepositoryFactory.createRepository("ProdsP")

    //Atributo del nombre del usuario
    private val _userName = MutableLiveData<String>("Juan Prueba")
    val userName: LiveData<String> = _userName

    //Atributo del image
    private val _prodImage = MutableLiveData<String>("https://firebasestorage.googleapis.com/v0/b/campustrade-6d7b6.appspot.com/o/images%2F1684466533667?alt=media&token=8b882630-9a80-4f10-8095-84aff563dc7b")
    val prodImage: LiveData<String> = _prodImage

    //Atributo del image
    private val _prodName = MutableLiveData<String>("")
    val prodName: LiveData<String> = _prodName

    //Atributo del precio del product
    private val _prodPrice = MutableLiveData<String>("")
    val prodPrice: LiveData<String> = _prodPrice

    //Atributo de los tags
    private val _prodTag = MutableLiveData<String>("")
    val prodTag: LiveData<String> = _prodTag



    fun onNetworkStateChanged(connection:Boolean){

    }






}
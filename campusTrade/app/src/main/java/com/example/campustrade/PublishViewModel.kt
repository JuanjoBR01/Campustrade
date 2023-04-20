package com.example.campustrade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PublishViewModel :ViewModel(){

    //Atributo del nombre del producto a agregar
    private val _prodName= MutableLiveData<String>()
    val prodName:LiveData<String> = _prodName

    //Atributo del precio del producto a agregar
    private val _prodPrice= MutableLiveData<String>()
    val prodPrice:LiveData<String> = _prodPrice

    //Atributo del description del producto a agregar
    private val _prodDescr= MutableLiveData<String>()
    val prodDescr:LiveData<String> = _prodDescr

    //Atributo del Tags del producto a agregar
    private val _prodTags= MutableLiveData<String>()
    val prodTags:LiveData<String> = _prodTags

    //Atributo de new o old del producto a agregar
    private val _selectedItem= MutableLiveData<String>()
    val selectedItem:LiveData<String> = _selectedItem

    fun onPublishChanged(pName:String,pPrice:String,pDescrip:String,pTags:String,pSelectedIt:String){
        //Change value if restrictions are correct
        if(pName.length <= 30){
            _prodName.value = pName
        }

        if(pPrice.length <= 9){
            _prodPrice.value = pPrice
        }

        if(pDescrip.length <= 250){
            _prodDescr.value = pDescrip
        }

        _prodTags.value = pTags
        _selectedItem.value = pSelectedIt
    }





}
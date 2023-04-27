package com.example.campustrade.publish

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campustrade.dtos.ProductObj
import com.example.campustrade.repository.PublishRepository
import com.example.campustrade.repository.RepositoryFactory
import com.example.campustrade.repository.RepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

class PublishViewModel :ViewModel(){

    //Conection to DB
    private val repository: RepositoryInterface = RepositoryFactory.createRepository("Publish")

    //Atributo del nombre del producto a agregar
    private val _prodName= MutableLiveData<String>(" ")
    val prodName:LiveData<String> = _prodName

    //Atributo del precio del producto a agregar
    private val _prodPrice= MutableLiveData<String>("0")
    val prodPrice:LiveData<String> = _prodPrice

    //Atributo del description del producto a agregar
    private val _prodDescr= MutableLiveData<String>(" ")
    val prodDescr:LiveData<String> = _prodDescr

    //Atributo del Tags del producto a agregar
    private val _prodTags= MutableLiveData<String>(" ")
    val prodTags:LiveData<String> = _prodTags

    //Atributo de new o old del producto a agregar
    private val _selectedItem= MutableLiveData<String>(" ")
    val selectedItem:LiveData<String> = _selectedItem

    //Atributo de la ruta de la imagen del producto
    private val _contentImage= MutableLiveData<Uri?>()
    val contentImage:LiveData<Uri?> = _contentImage

    //Atributo del scope
    private val _scope= MutableLiveData<CoroutineScope>()
    val scope:LiveData<CoroutineScope> = _scope

    //Atributo de si esta expandido
    private val _expanded= MutableLiveData<Boolean>(false)
    val expanded:LiveData<Boolean> = _expanded

    //Tipo de producto
    private val _valueType = MutableLiveData<String>(" ")
    val valueType:LiveData<String> = _valueType

    //Cargando
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading:LiveData<Boolean> = _isLoading

    //Boton habilitado
    private val _isAvailable = MutableLiveData<Boolean>(false)
    val isAvailable:LiveData<Boolean> = _isAvailable

    //If products where uploaded to DB
    private val _prodUpl = MutableLiveData<Boolean>(false)
    val prodUpl:LiveData<Boolean> = _prodUpl

    //Show Uploaded dialogs
    private val _prodDialg = MutableLiveData<Boolean>(false)
    val prodDialg:LiveData<Boolean> = _prodDialg

    //Atributo del state
    @OptIn(ExperimentalMaterialApi::class)
    private val _state= MutableLiveData<ModalBottomSheetState>()
    @OptIn(ExperimentalMaterialApi::class)
    val state:LiveData<ModalBottomSheetState> = _state

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

        if(pTags.length <= 100){
            _prodTags.value = pTags
        }
        _selectedItem.value = pSelectedIt

        if (_prodName.value != " " && _prodPrice.value != "0" && _prodDescr.value != " " && _prodTags.value != " " && _selectedItem.value != " "){
            _isAvailable.value = true
        }
    }

    fun onChangeComboBox(pValueType:String, pExpanded:Boolean){
        _valueType.value = pValueType
        _expanded.value = pExpanded
    }

    fun onChangeImage(pContentImage:Uri?){
        _contentImage.value = pContentImage
    }

    fun onChangeIsLoading(pLoading:Boolean){
        _isLoading.value = pLoading
    }

    fun onUploadProduct(productObj: ProductObj,bitmp:Bitmap){
        viewModelScope.launch{
            val objImg:ProductObj = repository.uploadImageToDataBase(productObj, bitmp)
            if (objImg.image != " ") {
                _prodUpl.value = repository.uploadProductToDB(objImg)
                onChangeIsLoading(false)
                _prodUpl.value = true
                onCloseDialog(true)
            }
            else{
                onChangeIsLoading(false)
                _prodUpl.value = false
                onCloseDialog(true)
            }
        }
    }

    fun onCloseDialog(dia:Boolean){
        _prodDialg.value = dia
    }
}
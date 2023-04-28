package com.example.campustrade.publish

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.*
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campustrade.dtos.ProductObj
import com.example.campustrade.repository.RepositoryFactory
import com.example.campustrade.repository.RepositoryInterface
import kotlinx.coroutines.*
import java.util.*

class PublishViewModel() : ViewModel() {

    //Conection to DB
    private val repository: RepositoryInterface = RepositoryFactory.createRepository("Publish")

    //Atributo del nombre del producto a agregar
    private val _prodName = MutableLiveData<String>("")
    val prodName: LiveData<String> = _prodName

    //Atributo del precio del producto a agregar
    private val _prodPrice = MutableLiveData<String>("")
    val prodPrice: LiveData<String> = _prodPrice

    //Atributo del description del producto a agregar
    private val _prodDescr = MutableLiveData<String>("")
    val prodDescr: LiveData<String> = _prodDescr

    //Atributo del Tags del producto a agregar
    private val _prodTags = MutableLiveData<String>("")
    val prodTags: LiveData<String> = _prodTags

    //Atributo de new o old del producto a agregar
    private val _selectedItem = MutableLiveData<String>("")
    val selectedItem: LiveData<String> = _selectedItem

    //Atributo de la ruta de la imagen del producto
    private val _contentImage = MutableLiveData<Uri?>()
    val contentImage: LiveData<Uri?> = _contentImage

    //Atributo del scope
    private val _scope = MutableLiveData<CoroutineScope>()
    val scope: LiveData<CoroutineScope> = _scope

    //Atributo de si esta expandido
    private val _expanded = MutableLiveData<Boolean>(false)
    val expanded: LiveData<Boolean> = _expanded

    //Tipo de producto
    private val _valueType = MutableLiveData<String>("")
    val valueType: LiveData<String> = _valueType

    //Cargando
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    //Boton habilitado
    private val _isAvailable = MutableLiveData<Boolean>(false)
    val isAvailable: LiveData<Boolean> = _isAvailable

    //If products where uploaded to DB
    private val _prodUpl = MutableLiveData<Boolean>(false)
    val prodUpl: LiveData<Boolean> = _prodUpl

    //Show Uploaded dialogs
    private val _prodDialg = MutableLiveData<Boolean>(false)
    val prodDialg: LiveData<Boolean> = _prodDialg

    //NetWork State
    private val _networkState = MutableLiveData<Boolean>(false)
    val networkState: LiveData<Boolean> = _networkState

    //Show internet connection dialogs
    private val _connectDialg = MutableLiveData<Boolean>(false)
    val connectDialg: LiveData<Boolean> = _connectDialg

    // Message dialog
    private val _message = MutableLiveData<String>("")
    val message: LiveData<String> = _message

    //Bitmap for image
    private var bitmapImage: Bitmap? = null


    //Atributo del state
    @OptIn(ExperimentalMaterialApi::class)
    private val _state = MutableLiveData<ModalBottomSheetState>()

    @OptIn(ExperimentalMaterialApi::class)
    val state: LiveData<ModalBottomSheetState> = _state

    fun onPublishChanged(
        pName: String,
        pPrice: String,
        pDescrip: String,
        pTags: String,
        pSelectedIt: String
    ) {
        //Change value if restrictions are correct
        if (pName.length <= 30) {
            _prodName.value = pName
        }

        if (pPrice.length <= 9) {
            _prodPrice.value = pPrice
        }

        if (pDescrip.length <= 250) {
            _prodDescr.value = pDescrip
        }

        if (pTags.length <= 100) {
            _prodTags.value = pTags
        }
        _selectedItem.value = pSelectedIt

        onActivateButton(true)


    }

    fun onChangeComboBox(pValueType: String, pExpanded: Boolean) {
        _valueType.value = pValueType
        _expanded.value = pExpanded
    }

    fun onChangeImage(pContentImage: Uri?, context: Context) {
        bitmapImage = null
        _contentImage.value = pContentImage
        viewModelScope.launch { transformImage(pContentImage, context) }
    }

    private suspend fun transformImage(pContentImage: Uri?, context: Context) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            async {
                val inputStream = context.contentResolver.openInputStream(pContentImage!!)
                bitmapImage = BitmapFactory.decodeStream(inputStream)
            }

        }

    }


    fun onChangeIsLoading(pLoading: Boolean) {
        _isLoading.value = pLoading
    }

    fun onActivateButton(pActive: Boolean) {
        if (pActive == false) {
            _isAvailable.value = pActive
        } else {
            if (_prodName.value != "" && _prodPrice.value != "0" && _prodDescr.value != "" && _prodTags.value != "" && _selectedItem.value != "" && _networkState.value == true) {
                _isAvailable.value = true
            }
        }
    }

    fun onUploadProduct(productObj: ProductObj) {
        viewModelScope.launch {
            if (_networkState.value!!) {
                val objImg: ProductObj = repository.uploadImageToDataBase(productObj, bitmapImage!!)
                if (objImg.image != " ") {
                    _prodUpl.value = repository.uploadProductToDB(objImg)
                    _message.value = "Product uploaded successfully to DB"
                    onChangeIsLoading(false)
                    _prodUpl.value = true
                    onCloseDialog(true)
                } else {
                    _message.value = "Unable to upload product to the DB, try again."
                    onChangeIsLoading(false)
                    _prodUpl.value = false
                    onCloseDialog(true)
                }
            } else {
                _message.value = "No internet connection, please try again later"
                onChangeIsLoading(false)
                _prodUpl.value = false
                onCloseDialog(true)
            }
        }
    }

    fun onCloseDialog(dia: Boolean) {
        _prodDialg.value = dia
    }

    fun onNetworkStateChanged(pIsConnected: Boolean) {
        _networkState.postValue(pIsConnected)
        if (!pIsConnected && _isLoading.value == true) {
            _message.postValue("No internet connection, product will be uploaded when connection returns if app is still running.")
            _connectDialg.postValue(true)
        } else {
            _message.postValue("No internet connection, connect and try again.")
            _connectDialg.postValue(true)
        }
    }
}
package com.example.campustrade.prodsProfile

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campustrade.dtos.UsersProdsObj
import com.example.campustrade.repository.RepositoryFactory
import com.example.campustrade.repository.RepositoryInterface
import com.google.firebase.firestore.auth.User
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
    private val _prodImage = MutableLiveData<String>("")
    val prodImage: LiveData<String> = _prodImage

    //Atributo del image
    private val _prodName = MutableLiveData<String>("No Name")
    val prodName: LiveData<String> = _prodName

    //Atributo del precio del product
    private val _prodPrice = MutableLiveData<String>("No Price")
    val prodPrice: LiveData<String> = _prodPrice

    //Atributo de los tags
    private val _prodTag = MutableLiveData<String>("No Tags")
    val prodTag: LiveData<String> = _prodTag

    //Areglo con los nombres de los posibles usuarios
    var userNames = arrayOf("Select")

    //Areglo con las imagenes
    var prodImages = arrayOf("Select")

    //Atributo de si esta expandido
    private val _expanded = MutableLiveData<Boolean>(false)
    val expanded: LiveData<Boolean> = _expanded

    //Atributo de la imagen del producto
    private val _prodImgg = MutableLiveData<String>("")
    val prodImgg: LiveData<String> = _prodImgg

    //Areglo con los nombres de los posibles usuarios
    var prodsNames = arrayOf("")

    //Areglo con los nombres de los posibles usuarios
    var prodPrices = arrayOf("")

    //Areglo con los nombres de los posibles usuarios
    var prodTags = arrayOf("")

    //Areglo con los nombres de los posibles usuarios
    var prodImagess = arrayOf("")

    //Arreglo que mapea los productos con su usuario responsable
    var userProduc = arrayOf("")

    //Areglo que tiene los items a mostrar
    var showNames = arrayOf("")

    //Areglo que tiene los items a mostrar
    var showPrices = arrayOf("")

    //Areglo que tiene los items a mostrar
    var tags = arrayOf("")

    //Areglo que tiene los items a mostrar
    var imgssss = arrayOf("")

    //Atributo del indice de producto
    private val _indeProd = MutableLiveData<Int>(0)
    val indeProd: LiveData<Int> = _indeProd


    //NetWork State
    private val _networkState = MutableLiveData<Boolean>(false)
    val networkState: LiveData<Boolean> = _networkState

    //Show internet connection dialogs
    private val _connectDialg = MutableLiveData<Boolean>(true)
    val connectDialg: LiveData<Boolean> = _connectDialg


    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    }

    fun saveToSharedPreferences(context: Context, key: String, value: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
        Log.d(TAG,"-------SAVEEE---------" + key + value)
    }

    fun retrieveFromSharedPreferences(context: Context, key: String): String? {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString(key, null)
    }


    fun changeActualValues(pUsername:String, pProdName: String, pProdPrice:String, pProdTag:String, pIndex:Int){
        _userName.value = pUsername
        _prodImage.value = ""
        _prodName.value = pProdName
        _prodPrice.value = pProdPrice
        _prodTag.value = pProdTag
        _indeProd.value = pIndex
        _prodImgg.value = ""
        Log.d(TAG,"----------------" + pProdName)
    }


    fun onNetworkStateChanged(pIsConnected:Boolean){
        _networkState.postValue(pIsConnected)
        if (!pIsConnected) {
            _connectDialg.postValue(true)
        }
    }


    fun onChangeComboBox(pProdName: String, pExpanded: Boolean) {
        if(networkState.value!!){
        for(index in userNames.indices){
            if(pProdName == userNames[index]){
                _userName.value = userNames[index]
                _prodImage.value = prodImages[index]
            }
        }
        showNames = arrayOf("No Name")
        showPrices = arrayOf("No Price")
        tags = arrayOf("No Tag")
        imgssss = arrayOf(" ")
        for(index in userProduc.indices){
            if(_userName.value == userProduc[index]){
                showNames += prodsNames[index]
                showPrices += prodPrices[index]
                tags += prodTags[index]
                imgssss += prodImagess[index]
            }
        }
        _prodName.value = showNames[0]
        _prodPrice.value = showPrices[0]
        _prodTag.value = tags[0]
        _indeProd.value = 0
        _prodImgg.value = imgssss[0]


        _expanded.value = pExpanded
    }
    }

    fun minusIndex(){
        if (_indeProd.value!! > 0){
            _indeProd.value = _indeProd.value!! - 1
            _prodName.value = showNames[_indeProd.value!!]
            _prodPrice.value = showPrices[_indeProd.value!!]
            _prodTag.value = tags[_indeProd.value!!]
            _prodImgg.value = imgssss[_indeProd.value!!]
        }

    }

    fun plusIndex(){
        if (_indeProd.value!! + 1 < showNames.size){
            _indeProd.value = _indeProd.value!! + 1
            _prodName.value = showNames[_indeProd.value!!]
            _prodPrice.value = showPrices[_indeProd.value!!]
            _prodTag.value = tags[_indeProd.value!!]
            _prodImgg.value = imgssss[_indeProd.value!!]
        }
    }


    fun onInicialization(){
        fetchMyObjects()
        fetchMyProds()
    }

    fun giveUserNames():Array<String>{
        return userNames
    }

    @SuppressLint("SuspiciousIndentation")
    fun fetchMyObjects() {
        val scope = CoroutineScope(Dispatchers.IO)
            scope.launch(Dispatchers.IO) {
                val myObjects = repository.getMyObjects()
                val names = myObjects.map{it.name}
                for(name in names){
                    userNames = userNames.plusElement(name)
                }
                val images = myObjects.map{it.image}
                for(image in images){
                    prodImages = prodImages.plusElement(image)
                }
            }

    }

    fun fetchMyProds() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch(Dispatchers.IO) {
            val myObjects = repository.getDataP()
            val names = myObjects.map{it.name}
            for(name in names){
                prodsNames = prodsNames.plusElement(name)
            }
            val prices = myObjects.map{it.price}
            for(pri in prices){
                val aux = pri.toString()
                prodPrices = prodPrices.plusElement(aux)
            }
            val tagss = myObjects.map{it.tags}
            for(ta in tagss){
                prodTags = prodTags.plusElement(ta)
            }
            val imagesProds = myObjects.map{it.image}
            for(ip in imagesProds){
                prodImagess = prodImagess.plusElement(ip)
            }
            val usna = myObjects.map{it.user}
            for(name in usna){
                userProduc = userProduc.plusElement(name)
            }
        }

    }






}
package com.example.campustrade.history

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campustrade.*
import com.example.campustrade.dtos.PurchaseInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class HistoryViewModel(private val repository: HistoryRepository): ViewModel() {

    private var _productList = MutableLiveData<List<ProductDB>>()
    val productList : LiveData<List<ProductDB>> = _productList

    fun arrangeProductListFirestore()=viewModelScope.launch{
        var productList = arrayListOf<ProductDB>()
        var finalList = arrayListOf<ProductDB>()

        withContext(Dispatchers.IO){
                val actualList = repository.getData()
                _productList.postValue(actualList)
        }
    }

    private val _numPur = MutableLiveData<String>()
    val numPur : LiveData<String> = _numPur

    private val _totPur = MutableLiveData<String>()
    val totPur : LiveData<String> = _totPur

    private val _typePur = MutableLiveData<String>()
    val typePur : LiveData<String> = _typePur

    private val _condPur = MutableLiveData<String>()
    val condPur : LiveData<String> = _condPur

    fun purchaseData(contexto: Context) = viewModelScope.launch{

        withContext(Dispatchers.IO) {
                try {
                    val actualInfo = repository.getPurchaseData()?.get(0)
                    if (actualInfo != null) {
                        _numPur.postValue(actualInfo.numPurchases.toString())
                        _totPur.postValue(actualInfo.totalPurchased.toString())
                        _typePur.postValue(actualInfo.typePurchased)
                        _condPur.postValue(actualInfo.condPurchased)

                        saveToSharedPreferences(
                            contexto,
                            "numPur",
                            actualInfo.numPurchases.toString()
                        )
                        saveToSharedPreferences(
                            contexto,
                            "totPur",
                            actualInfo.totalPurchased.toString()
                        )
                        saveToSharedPreferences(contexto, "typePur", actualInfo.typePurchased)
                        saveToSharedPreferences(contexto, "condPur", actualInfo.condPurchased)
                    }
                }catch(e: Exception) {

                    if (retrieveFromSharedPreferences(contexto, "numPur") == "") {
                        _numPur.postValue("NO")
                    } else {

                        val numPurP = retrieveFromSharedPreferences(contexto, "numPur")
                        if (numPurP != null) {
                            _numPur.postValue(numPurP!!)
                        }
                        val totPurP = retrieveFromSharedPreferences(contexto, "totPur")
                        if (totPurP != null) {
                            _totPur.postValue(totPurP!!)
                        }
                        val typePurP = retrieveFromSharedPreferences(contexto, "typePur")
                        if (typePurP != null) {
                            _typePur.postValue(typePurP!!)
                        }
                        val condPurP = retrieveFromSharedPreferences(contexto, "condPur")
                        if (condPurP != null) {
                            _condPur.postValue(condPurP!!)
                        }
                    }
                }
        }
    }

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    }

    fun saveToSharedPreferences(context: Context, key: String, value: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun retrieveFromSharedPreferences(context: Context, key: String): String? {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString(key, null)
    }

}
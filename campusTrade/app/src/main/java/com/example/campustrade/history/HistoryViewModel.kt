package com.example.campustrade.history

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.campustrade.*
import com.example.campustrade.dtos.PurchaseInfo
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HistoryViewModel(private val repository: HistoryRepository) {

    private var _productList = MutableLiveData<List<ProductDB>>()
    val productList : LiveData<List<ProductDB>> = _productList

    fun arrangeProductListFirestore(){
        var productList = arrayListOf<ProductDB>()
        var finalList = arrayListOf<ProductDB>()

        runBlocking{
            launch{
                val actualList = repository.getData()
                _productList.value =  actualList
            }
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

    fun purchaseData(contexto: Context)
    {
        runBlocking{
            launch{
                val actualInfo = repository.getPurchaseData()?.get(0)
                if(actualInfo != null)
                {
                    _numPur.value =  actualInfo.numPurchases.toString()
                    _totPur.value =  actualInfo.totalPurchased.toString()
                    _typePur.value =  actualInfo.typePurchased
                    _condPur.value =  actualInfo.condPurchased

                    saveToSharedPreferences(contexto, "numPur", actualInfo.numPurchases.toString())
                    saveToSharedPreferences(contexto, "totPur", actualInfo.totalPurchased.toString())
                    saveToSharedPreferences(contexto, "typePur", actualInfo.typePurchased)
                    saveToSharedPreferences(contexto, "condPur", actualInfo.condPurchased)
                }
                else
                {
                    val numPurP = retrieveFromSharedPreferences(contexto, "numPur")
                    if(numPurP != null)
                    {
                        _numPur.value =  numPurP!!
                    }
                    val totPurP = retrieveFromSharedPreferences(contexto, "totPur")
                    if(totPurP != null)
                    {
                        _totPur.value =  totPurP!!
                    }
                    val typePurP = retrieveFromSharedPreferences(contexto, "typePur")
                    if(typePurP != null)
                    {
                        _typePur.value =  typePurP!!
                    }
                    val condPurP = retrieveFromSharedPreferences(contexto, "condPur")
                    if(condPurP != null)
                    {
                        _condPur.value =  condPurP!!
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
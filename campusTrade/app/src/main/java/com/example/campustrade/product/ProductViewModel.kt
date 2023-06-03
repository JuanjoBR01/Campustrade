package com.example.campustrade.product

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campustrade.ProductDB
import com.example.campustrade.cart.CartActivity
import com.example.campustrade.home.HomeActivityMVVM
import com.example.campustrade.objects.ExpirationCache.expiringCacheApp
import com.example.campustrade.objects.LruCacheCampus.lruCacheApp
import com.example.campustrade.objects.LruCacheCant.lruCacheCant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

class ProductViewModel(private val repository: ProductRepository): ViewModel() {

    private var _productInfo = MutableLiveData<ProductDB>()
    val productInfo : LiveData<ProductDB> = _productInfo


    fun getProductDB(contexto: Context) = viewModelScope.launch {

        withContext(Dispatchers.IO) {
            val actualList = repository.getData()

            val nombreProd = retrieveFromSharedPreferences(contexto, "prodDetail")

            if(nombreProd != null){
                val prodInfo = expiringCacheApp.get(nombreProd)
                if(prodInfo != null)
                {
                    _productInfo.postValue(prodInfo!! as ProductDB?)
                }
                else{
                    actualList.forEach { producto ->
                        if (producto.name == nombreProd) {
                            _productInfo.postValue(producto)
                            return@forEach
                        }
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

    fun backPage(context: Context) {
        val intent = Intent(context, HomeActivityMVVM::class.java)
        context.startActivity(intent)
    }

    fun addToCart(context: Context, product: ProductDB) {
        lruCacheApp.put(product.name, product)

        if(lruCacheCant.get(product.name) == null)
        {
            lruCacheCant.put(product.name, 1)
        }
        else
        {
            lruCacheCant.put(product.name, lruCacheCant.get(product.name)!! + 1)
        }

        val intent = Intent(context, CartActivity::class.java)
        context.startActivity(intent)
    }

    fun goToCart(context: Context) {
        val intent = Intent(context, CartActivity::class.java)
        context.startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDiscount(): Int {
        val date = LocalDate.now()
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)

        var discount = 10

        if(dayOfWeek == "Monday" || dayOfWeek == "Tuesday" || dayOfWeek == "Wednesday")
        {
            discount = 30
        }
        else if(dayOfWeek == "Thursday" || dayOfWeek == "Friday")
        {
            discount = 20
        }

        return discount
    }

}
package com.example.campustrade.cart

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campustrade.ProductDB
import com.example.campustrade.SortingProductsStrategyType
import com.example.campustrade.StrategyContext
import com.example.campustrade.home.HomeActivityMVVM
import com.example.campustrade.objects.LruCacheCampus.lruCacheApp
import com.example.campustrade.objects.LruCacheCant.lruCacheCant
import com.example.campustrade.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class CartViewModel(): ViewModel() {

    private var _productList = MutableLiveData<List<ProductDB>>()
    val productList : LiveData<List<ProductDB>> = _productList

    private var _codigo = MutableLiveData<String>()
    val codigo : LiveData<String> = _codigo

    fun getLRU() = viewModelScope.launch{
        withContext(Dispatchers.Default) {
            val lista = lruCacheApp.getAllValues().toList() as List<ProductDB>

            val strategy = StrategyContext(SortingProductsStrategyType())

            val listaOrg = strategy.executeStrategy(preference, lista)

            _productList.postValue(listaOrg)
        }
    }

    fun deleteFromLRU(producto: ProductDB){
        lruCacheApp.delete(producto.name)
        lruCacheCant.delete(producto.name)
        val lista = lruCacheApp.getAllValues().toList() as List<ProductDB>
        _productList.postValue(lista)
    }

    fun getLRUCant(producto: ProductDB): Int? {
        val cant = lruCacheCant.get(producto.name)
        return cant
    }

    fun checkFile(context: Context) = viewModelScope.launch{
        withContext(Dispatchers.IO) {
            val filename = "codigo.txt"

            try {

                val fileInputStream = context.openFileInput(filename)
                val fileBytes = fileInputStream.readBytes()
                fileInputStream.close()

                val fileContent = fileBytes.toString(Charsets.UTF_8)
                _codigo.postValue(fileContent)

            } catch (e: Exception) {
                // Handle any exceptions that occur during file write
                _codigo.postValue("")
            }
        }
    }

    fun saveToFile(context: Context, codigoP: String) = viewModelScope.launch{
        withContext(Dispatchers.IO) {
            val filename = "codigo.txt"
            val fileContents = codigoP

            try {
                val fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
                fileOutputStream.write(fileContents.toByteArray())
                fileOutputStream.close()

                val fileInputStream = context.openFileInput(filename)
                val fileBytes = fileInputStream.readBytes()
                fileInputStream.close()

                val fileContent = fileBytes.toString(Charsets.UTF_8)

                _codigo.postValue(fileContent)

            } catch (e: Exception) {
                // Handle any exceptions that occur during file write
            }
        }
    }

    fun backPage(context: Context) {
        val intent = Intent(context, HomeActivityMVVM::class.java)
        context.startActivity(intent)
    }

    val preference = "Accesory"

}
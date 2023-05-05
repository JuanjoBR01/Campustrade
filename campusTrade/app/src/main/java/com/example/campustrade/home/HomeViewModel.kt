package com.example.campustrade.home

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campustrade.*
import com.example.campustrade.history.HistoryActivity
import com.example.campustrade.profile.ProfileScreen
import com.example.campustrade.publish.PublishScreen
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeViewModel(private val repository: HomeRepository): ViewModel() {

    private val _preference = MutableLiveData<String>()
    val preference : LiveData<String> = _preference

    private var _productList = MutableLiveData<List<ProductDB>>()
    val productList : LiveData<List<ProductDB>> = _productList

    init{
        viewModelScope.launch {
            //_productList.value = repository.getData()
        }
    }

    private val _value = MutableLiveData<String>()
    val value : LiveData<String> = _value

    val maxChar = 25

    fun onSearchChange(value: String){
        if (value.length <= maxChar)
        {
            _value.value = value
        }
    }

    fun onPreferenceChange(value: String){
        if(value != "Used" && value != "New")
        {
            _preference.value = "Accesory"
        }
        else{
            _preference.value = value
        }
    }

    fun prepareProductList(): List<Product> {
        val productList = arrayListOf<Product>()

        // add menu items
        productList.add(Product(name = "Lab Coat", type = "Accesory", price = 30000, icon = Icons.Filled.Star, date = "24/03/2022" ))
        productList.add(Product(name = "Arduino", type = "Product", price = 400000, icon = Icons.Filled.Phone, date = "24/03/2022"))
        productList.add(Product(name = "Pencil", type = "Material", price = 5000, icon = Icons.Filled.Create, date = "24/03/2022"))
        productList.add(Product(name = "Sneakers", type = "Accesory", price = 100000, icon = Icons.Filled.Star, date = "24/03/2022"))
        productList.add(Product(name = "Lab Mask", type = "Accesory", price = 500000, icon = Icons.Filled.Star, date = "24/03/2022"))
        productList.add(Product(name = "Screwdriver Set", type = "Product", price = 20000, icon = Icons.Filled.Phone, date = "24/03/2022"))

        return productList
    }

    fun arrangeProductList(search: String): List<Product>{
        val productList = arrayListOf<Product>()
        var finalList = arrayListOf<Product>()
        val actualList = prepareProductList()

        val preference = "Accesory"

        actualList.forEach { producto ->
            if(producto.type == preference)
            {
                productList.add(producto)
            }
        }
        actualList.forEach { producto ->
            if(producto.type != preference)
            {
                productList.add(producto)
            }
        }

        if(search != "")
        {
            productList.forEach { producto ->
                if(producto.name.contains(search))
                {
                    finalList.add(producto)
                }
            }
        }
        else{
            finalList = productList
        }

        return finalList
    }

    fun arrangeProductListFirestore(search: String, preference: String){
        var productList = arrayListOf<ProductDB>()
        var finalList = arrayListOf<ProductDB>()

        runBlocking{
            launch{
                val actualList = repository.getData()

                //val preference = "Used"
                val strategy = StrategyContext(SortingProductsStrategyType())

                //val strategy = SortingProductsStrategyType()

                if(preference == "Used"){
                    strategy.setStrategy(SortingProductsStrategyCondition())
                }
                if(preference == "New"){
                    strategy.setStrategy(SortingProductsStrategyCondition())
                }

                productList = strategy.executeStrategy(preference, actualList)


                if(search != "")
                {
                    productList.forEach { producto ->
                        if(producto.name.contains(search))
                        {
                            finalList.add(producto)
                        }
                    }
                }
                else{
                    finalList = productList
                }

                _productList.value =  finalList
            }
        }
    }

    data class Product2(val name: String, val type: String, val price: Int, val icon: ImageVector)

    fun returnImage(nombre: String): Int {
        if(nombre == "Lab Coat") return R.drawable.lab_coat
        else if(nombre == "Sneakers") return R.drawable.sneakers
        else if(nombre == "Lab Mask") return R.drawable.lab_mask
        else if(nombre == "Arduino") return R.drawable.arduino
        else if(nombre == "Pencils") return R.drawable.pencils
        else if(nombre == "Screwdriver Set") return R.drawable.screwdriver_set
        return R.drawable.product_sample
    }

    fun returnColor(color: String): Color {
        if(color == "anaranjado")
            return Color(0xFFFB8500)
        else
            return Color(0xFF023047)
    }

    fun prepareBottomMenu(): List<BottomMenuItem> {
        val bottomMenuItemsList = arrayListOf<BottomMenuItem>()

        // add menu items
        bottomMenuItemsList.add(BottomMenuItem(label = "Home", icon = Icons.Filled.Home))
        bottomMenuItemsList.add(BottomMenuItem(label = "Explore", icon = Icons.Filled.List))
        bottomMenuItemsList.add(BottomMenuItem(label = "Publish", icon = Icons.Filled.Add))
        bottomMenuItemsList.add(BottomMenuItem(label = "History", icon = Icons.Filled.ShoppingCart))
        bottomMenuItemsList.add(BottomMenuItem(label = "Profile", icon = Icons.Filled.Person))

        return bottomMenuItemsList
    }

    data class BottomMenuItem2(val label: String, val icon: ImageVector)

    fun changePage(name: String, context: Context){
        if(name == "Home")
        {
            val intent = Intent(context, HomeActivityMVVM::class.java)
            context.startActivity(intent)
        }
        else if(name == "Publish")
        {
            val intent = Intent(context, PublishScreen::class.java)
            context.startActivity(intent)
        }
        else if(name == "History")
        {
            val intent = Intent(context, HistoryActivity::class.java)
            context.startActivity(intent)
        }
        else if(name == "Profile")
        {
            val intent = Intent(context, ProfileScreen::class.java)
            context.startActivity(intent)
        }

    }

}
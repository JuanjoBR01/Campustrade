package com.example.campustrade.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campustrade.*
import com.example.campustrade.explore.ExploreScreen
import com.example.campustrade.history.HistoryActivity
import com.example.campustrade.history.HistoryRepository
import com.example.campustrade.objects.CurrentUser
import com.example.campustrade.profile.ProfileScreen
import com.example.campustrade.publish.PublishScreen
import com.example.campustrade.repository.TelemetryRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

class HomeViewModel(private val repository: HomeRepository, private val historyRepository: HistoryRepository): ViewModel() {

    private val _preference = MutableLiveData<String>()
    val preference : LiveData<String> = _preference

    private var _productList = MutableLiveData<List<ProductDB>>()
    val productList : LiveData<List<ProductDB>> = _productList

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

        var preference = CurrentUser.user!!.tag

        if(preference == "Accesory" || preference == "Material" || preference == "Product" || preference == "Other")
        {
            preference = preference
        }
        else
        {
            preference = "Accesory"
        }
        //val preference = "Accesory"

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

    fun arrangeProductListFirestore(search: String, preference: String){
        var productList = arrayListOf<ProductDB>()
        var finalList = arrayListOf<ProductDB>()

        runBlocking{
            launch{
                val actualList = repository.getData()

                val userList = historyRepository.getData()

                var preferenceResp = preference

                var contAcc = 0
                var contPro = 0
                var contMat = 0
                var contOth = 0

                userList.forEach{producto ->
                    if(producto.type == "Accesory")
                    {
                        contAcc++
                    }
                    else if(producto.type == "Material")
                    {
                        contMat++
                    }
                    else if(producto.type == "Product")
                    {
                        contPro++
                    }
                    else{
                        contOth++
                    }
                }

                if(contAcc >= contPro && contAcc >= contMat && contAcc >= contOth)
                {
                    preferenceResp = "Accesory"
                }
                else if(contPro >= contAcc && contPro >= contMat && contPro >= contOth)
                {
                    preferenceResp = "Product"
                }
                else if(contMat >= contAcc && contMat >= contPro && contMat >= contOth)
                {
                    preferenceResp = "Product"
                }
                else
                {
                    preferenceResp = "Other"
                }



                //val preference = "Used"
                val strategy = StrategyContext(SortingProductsStrategyType())

                //val strategy = SortingProductsStrategyType()

                if(preference == "Used"){
                    strategy.setStrategy(SortingProductsStrategyCondition())
                    preferenceResp = "Used"
                }
                if(preference == "New"){
                    strategy.setStrategy(SortingProductsStrategyCondition())
                    preferenceResp = "New"
                }

                productList = strategy.executeStrategy(preferenceResp, actualList)


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

        //val telemetryRepository = TelemetryRepository()

        if(name == "Home")
        {
            //telemetryRepository.updateVisits("Home")
            val intent = Intent(context, HomeActivityMVVM::class.java)
            context.startActivity(intent)

        }
        else if(name == "Publish")
        {
            //telemetryRepository.updateVisits("Publish")
            val intent = Intent(context, PublishScreen::class.java)
            context.startActivity(intent)
        }
        else if(name == "History")
        {
            //telemetryRepository.updateVisits("Transactions")
            val intent = Intent(context, HistoryActivity::class.java)
            context.startActivity(intent)
        }
        else if(name == "Profile")
        {
            //telemetryRepository.updateVisits("Profile")

            val intent = Intent(context, ProfileScreen::class.java)
            context.startActivity(intent)
        }
        else if(name == "Explore")
        {
            //telemetryRepository.updateVisits("Explore")
            val intent = Intent(context, ExploreScreen::class.java)
            context.startActivity(intent)
        }



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
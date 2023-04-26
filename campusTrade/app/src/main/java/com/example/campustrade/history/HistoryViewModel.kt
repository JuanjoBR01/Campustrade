package com.example.campustrade.history

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import com.example.campustrade.Product
import com.example.campustrade.R

class HistoryViewModel(private val repository: HistoryRepository) {

    fun returnImage(nombre: String): Int {
        if(nombre == "Lab Coat") return R.drawable.lab_coat
        else if(nombre == "Sneakers") return R.drawable.sneakers
        else if(nombre == "Lab Mask") return R.drawable.lab_mask
        else if(nombre == "Arduino") return R.drawable.arduino
        else if(nombre == "Pencils") return R.drawable.pencils
        else if(nombre == "Screwdriver Set") return R.drawable.screwdriver_set
        return R.drawable.sneakers
    }


    fun prepareTransactionList(): List<Product> {
        val productList = arrayListOf<Product>()

        // add menu items
        productList.add(Product(name = "Lab Coat", type = "Accesory", price = 30000, icon = Icons.Filled.Home, date = "24/08/2022" ))
        productList.add(Product(name = "Pencils", type = "Material", price = 5000, icon = Icons.Filled.Person, date = "12/03/2022"))
        productList.add(Product(name = "Sneakers", type = "Accesory", price = 100000, icon = Icons.Filled.DateRange, date = "02/01/2022"))
        return productList
    }

}
package com.example.campustrade.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campustrade.objects.Coordinates


class MapViewModel(): ViewModel(){

    private val _nearbyDistributors = MutableLiveData(0)
    val nearbyDistributors: LiveData<Int> = _nearbyDistributors

    private val _materials = MutableLiveData(0)
    val materials: LiveData<Int> = _materials

    private val _accessories = MutableLiveData(0)
    val accessories: LiveData<Int> = _accessories

    private val _products = MutableLiveData(0)
    val products: LiveData<Int> = _products

    private val _others = MutableLiveData(0)
    val others: LiveData<Int> = _others

    init {

        for (distributor in Coordinates.distributors) {
            if (distributor.city == Coordinates.userCity) {
                _nearbyDistributors.value = _nearbyDistributors.value?.plus(1)

                when (distributor.tag) {
                    "Material" -> {
                        _materials.value = _materials.value?.plus(1)
                    }
                    "Accessory" -> {
                        _accessories.value = _accessories.value?.plus(1)
                    }
                    "Product" -> {
                        _products.value = _products.value?.plus(1)
                    }
                    else -> {
                        _others.value = _others.value?.plus(1)
                    }
                }
            }



        }

    }
}
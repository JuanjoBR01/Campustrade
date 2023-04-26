package com.example.campustrade

interface SortingProductsStrategyInterface {

    fun execute(preference: String, actualList: List<ProductDB>): ArrayList<ProductDB>

}
package com.example.campustrade

class SortingProductsStrategyDate: SortingProductsStrategyInterface {

    override fun execute(preference: String, actualList: List<ProductDB>): ArrayList<ProductDB> {
        val productList = arrayListOf<ProductDB>()

        actualList.forEach { producto ->
            if(producto.type == preference && producto.name.length > 2)
            {
                productList.add(producto)
            }
        }
        return productList

    }

}
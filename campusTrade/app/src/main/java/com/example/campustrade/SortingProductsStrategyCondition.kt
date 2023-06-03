package com.example.campustrade

class SortingProductsStrategyCondition: SortingProductsStrategyInterface {

    override fun execute(preference: String, actualList: List<ProductDB>): ArrayList<ProductDB> {
        val productList = arrayListOf<ProductDB>()

        actualList.forEach { producto ->
            if(producto.condition == preference)
            {
                productList.add(producto)
            }
        }
        actualList.forEach { producto ->
            if(producto.condition != preference)
            {
                productList.add(producto)
            }
        }
        return productList
    }

}
package com.example.campustrade

class SortingProductsStrategyType: SortingProductsStrategyInterface {
    override fun execute(preference: String, actualList: List<ProductDB>): ArrayList<ProductDB> {
            val productList = arrayListOf<ProductDB>()

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
        return productList

    }
}
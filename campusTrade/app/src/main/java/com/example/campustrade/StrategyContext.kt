package com.example.campustrade

class StrategyContext(private var strategy: SortingProductsStrategyInterface) {

    fun executeStrategy(preference: String, actualList: List<ProductDB>): ArrayList<ProductDB> {
        return strategy.execute(preference, actualList)
    }

    fun setStrategy(newStrategy: SortingProductsStrategyInterface) {
        strategy = newStrategy
    }

}
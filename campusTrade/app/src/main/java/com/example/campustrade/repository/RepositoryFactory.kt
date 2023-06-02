package com.example.campustrade.repository

object RepositoryFactory {
    fun createRepository(type: String): RepositoryInterface {
        return when (type) {
            "Home" -> HomeRepository()
            "History" -> HistoryRepository()
            "Publish" -> PublishRepository()
            "Login" -> LoginRepository()
            "Singup" -> SignUpRepository()
            "ProdsP" -> ProdsPRepository()

            else -> throw IllegalArgumentException("Invalid repository type")
        }
    }
}

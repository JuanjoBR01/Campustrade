package com.example.campustrade.repository

object RepositoryFactory {
    fun createRepository(type: String): RepositoryInterface {
        return when (type) {
            "Publish" -> PublishRepository()
            else -> throw IllegalArgumentException("Invalid repository type")
        }
    }
}

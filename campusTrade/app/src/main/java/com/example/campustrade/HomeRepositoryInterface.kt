package com.example.campustrade

interface HomeRepositoryInterface {
    suspend fun getData(): List<ProductDB>
}
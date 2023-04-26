package com.example.campustrade.home

import com.example.campustrade.ProductDB

interface HomeRepositoryInterface {
    suspend fun getData(): List<ProductDB>
}
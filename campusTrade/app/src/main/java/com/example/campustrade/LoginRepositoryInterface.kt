package com.example.campustrade

interface LoginRepositoryInterface {
    suspend fun makeLogin(email: String,password: String): Boolean
}
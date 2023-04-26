package com.example.campustrade.login

interface LoginRepositoryInterface {
    suspend fun makeLogin(email: String,password: String): Boolean
}
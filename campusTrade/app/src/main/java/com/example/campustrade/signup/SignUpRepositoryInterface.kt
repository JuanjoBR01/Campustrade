package com.example.campustrade.signup

interface SignUpRepositoryInterface {
    suspend fun createUser(vt: String, nn: String, em: String, pw: String, imgUrl: String)
}
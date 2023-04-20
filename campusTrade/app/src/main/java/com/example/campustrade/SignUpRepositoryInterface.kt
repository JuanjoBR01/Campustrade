package com.example.campustrade

interface SignUpRepositoryInterface {
    suspend fun createUser(vt: String, nn: String, em: String, pw: String, imgUrl: String): Boolean
}
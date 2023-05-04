package com.example.campustrade.domain.model

import com.example.campustrade.dtos.UserObj
import com.example.campustrade.repository.AuthenticationRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val repository: AuthenticationRepository){
    suspend operator fun invoke(): List<UserObj>? {
        //val quotes = repository.getAllUsersFromLocalDatabase()

        //return quotes
        return null

    }
}
package com.example.campustrade.dtos

import com.example.campustrade.data.database.entities.UserEntity
data class UserObj(
    var name: String,
    var email: String,
    var tag: String,
    var image: String,
    val lastaccess: String){
    constructor(): this("","","","", "")
}

fun UserEntity.toDomain() = UserObj(name, email, tag, image, lastaccess)


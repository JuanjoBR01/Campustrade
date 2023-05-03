package com.example.campustrade.dtos

import android.os.Build

data class UserObj(
    var name: String,
    var email: String,
    var tag: String,
    var image: String,
    val lastaccess: String){

    constructor(): this("","","","", "String")

    fun changeImage(pImage:String){
        image = pImage
    }

}


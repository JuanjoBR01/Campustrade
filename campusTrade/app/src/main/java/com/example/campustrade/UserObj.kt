package com.example.campustrade

data class UserObj(
    var name: String,
    var email: String,
    var tag: String,
    var image: String){

    fun changeImage(pImage:String){
        image = pImage
    }

}


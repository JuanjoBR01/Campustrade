package com.example.campustrade


data class ProductObj(
    var image:String,
    var name:String,
    var price:Int,
    var description:String,
    var condition:String,
    var type:String,
    var tags:String
){
    fun changeImage(pImage:String){
        image = pImage
    }
}

package com.example.campustrade.dtos


data class ProductObj(
    var image:String,
    var name:String,
    var price:Int,
    var description:String,
    var condition:String,
    var type:String,
    var tags:String,
    var publishDate:String,
    var stock:Int = 1 ,
    var technicalSpecs:String
){
    fun changeImage(pImage:String){
        image = pImage
    }
}

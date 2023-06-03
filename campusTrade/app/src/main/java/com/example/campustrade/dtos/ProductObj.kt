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
    var technicalSpecs:String,
    var user:String
){
    constructor(): this("Prueba", "Prueba", 8, "Prueba","Prueba","Prueba","Prueba","",1,"Prueba","",)
    fun changeImage(pImage:String){
        image = pImage
    }
}

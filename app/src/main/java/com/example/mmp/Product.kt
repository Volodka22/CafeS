package com.example.mmp

class Product(type:String,name:String, shortText:String, price: Int){
    var type:String
    var name:String
    var shortText:String
    var price: Int = 0

    init {
        this.name = name
        this.price = price
        this.shortText = shortText
        this.type = type
    }
}
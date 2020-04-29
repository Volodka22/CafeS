package com.example.mmp

import java.io.Serializable

class User : Serializable {

    class Arr(string: String, int: Int) : Serializable{
        var name = string
        var count = int
    }

    var numberTable:Int = 0
    val array = mutableListOf<Arr>()
}
package org.lox.kotlindemo.model

import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class Data {
    var Name: String = ""
    var Type: String? // can null
    var Sum: Int = 0
    var CHANNEL_ID = "ch1"

    val Date: Calendar
//    val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy");

    constructor(name: String, type: String?){
        this.Name = name
        this.Type = type
        this.Date = Calendar.getInstance()
//        sdf.format(Date.time)
    }

    constructor(name: String, type: String?, sum: Int){
        this.Name = name
        this.Type = type
        this.Sum = sum
        this.Date = Calendar.getInstance()
    }

    fun print():String? {
        return "name : ${this.Name} type : ${this.Type} sum : ${this.Sum} "
    }
}
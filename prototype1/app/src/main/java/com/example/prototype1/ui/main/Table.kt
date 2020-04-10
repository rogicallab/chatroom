package com.example.prototype1.ui.main

class Table{
    lateinit var category:String
    lateinit var title:String
    constructor(){
    }
    constructor(category:String,title:String){
        this.category=category
        this.title=title
    }
    constructor(category:String,title:String,message:String){
        this.category=category
        this.title=title
    }

}
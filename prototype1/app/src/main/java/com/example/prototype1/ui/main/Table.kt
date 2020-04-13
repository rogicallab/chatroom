package com.example.prototype1.ui.main

class Table{
    lateinit var category:String
    lateinit var title:String
    var id :String=""
    constructor(){
    }
    constructor(category:String,title:String){
        this.category=category
        this.title=title
    }
    constructor(category:String,title:String,id:String){
        this.category=category
        this.title=title
        this.id=id
    }

}
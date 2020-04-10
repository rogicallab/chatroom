package com.example.prototype1.ChatRoom

import com.google.firebase.auth.FirebaseAuth

class Message (){
    var user:String=""
    var message:String=""
    var id :String=""
    constructor(author:String,message:String) : this() {
        this.user=author
        this.message=message

    }
    constructor(author:String,message:String,id:String) : this() {
        this.user=author
        this.message=message
        this.id=id
    }




    @SuppressWarnings("unused")
    private fun Message(){
    }
}
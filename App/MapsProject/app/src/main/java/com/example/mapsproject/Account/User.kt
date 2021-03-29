package com.example.mapsproject.Account

data class User(
    val uid : String? = null,
    val username:String? = null,
    val email:String? = null,
    val highscore:Int? = null

) {
    override fun toString(): String {
        return "uid: "+uid+"\nusername: "+username+"\nemail: "+email+"\nhighsocre: "+highscore
    }
}

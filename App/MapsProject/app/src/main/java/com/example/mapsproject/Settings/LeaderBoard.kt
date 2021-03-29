package com.example.mapsproject.Settings

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mapsproject.Account.Account
import com.example.mapsproject.Account.User
import com.example.mapsproject.R
import com.google.firebase.storage.StorageReference
import java.io.File

class LeaderBoard:Activity() {
    var element_numbers = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        val layout = findViewById<LinearLayout>(R.id.leaderboard_layout)

        val listLeaders = Account.getLeaderboard(element_numbers)
        Log.i("myTag","number of element in the list: "+listLeaders.size)
        val myView = LayoutInflater.from(applicationContext).inflate(R.layout.element_leaderboard,null)
        layout!!.addView(myView)/*
        for(elem in listLeaders){
            addElement(elem, layout)
        }*/

    }

    private fun addElement(elem: User, layout: LinearLayout?) {
        val myView = LayoutInflater.from(applicationContext).inflate(R.layout.element_leaderboard,null)
        /*myView.findViewById<TextView>(R.id.name_user).setText(elem.username)
        myView.findViewById<TextView>(R.id.score_user).setText(elem.highscore.toString())

        val storageRef = Account.storage.reference
        val path : String = "images/"+ elem.uid
        val  imagesRef: StorageReference? = storageRef.child(path)
        val imageView = findViewById<ImageView>(R.id.avatar)
        val localFile = File.createTempFile("images", "jpg")
        imagesRef?.getFile(localFile)?.addOnSuccessListener {
            // Local temp file has been created
            imageView.setImageBitmap((BitmapFactory.decodeFile(localFile.path)))
        }?.addOnFailureListener {
            // Handle any errors
            null
        }*/
        layout!!.addView(myView)
    }
}
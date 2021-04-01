package com.example.mapsproject.Settings

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import com.example.mapsproject.Account.Account
import com.example.mapsproject.Account.User
import com.example.mapsproject.R
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.File

class LeaderBoard:Activity() {
    var element_numbers = 3
    lateinit var snapshot: QuerySnapshot



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        val usersRef = Account.db.collection("users")

        GlobalScope.launch(Dispatchers.Main) { // launches coroutine in main thread
            snapshot = usersRef.orderBy("highscore", Query.Direction.DESCENDING).limit(element_numbers.toLong()).get().await()
            Log.i("myTag", "QuerySnapshot successfully executed!")
            updateUi()
        }

        findViewById<Button>(R.id.leaderboard_btn).setOnClickListener {

            val n = findViewById<EditText>(R.id.editTextNumber).text
            if (n != null && n.toString() != "") {
                element_numbers = n.toString().toInt()
            }
            GlobalScope.launch(Dispatchers.Main) { // launches coroutine in main thread
                snapshot = usersRef.orderBy("highscore", Query.Direction.DESCENDING).limit(element_numbers.toLong()).get().await()
                Log.i("myTag", "QuerySnapshot successfully executed!")
                updateUi()
            }

        }
    }

     private fun updateUi() {
        val layout = findViewById<LinearLayout>(R.id.leaderboard_layout)
         layout.removeAllViews()
        val  list = snapshot.documents
        Log.i("myTag","list length: "+list.size)
        for (document in list){
            val u = document.toObject<User>()
            addElement(u!!, layout)
        }
    }


     fun addElement(elem: User, layout: LinearLayout?) {
        Log.i("myTag","adding to layout: " +elem.toString() )
        val myView = LayoutInflater.from(applicationContext).inflate(R.layout.element_leaderboard,null)
        myView.findViewById<TextView>(R.id.name_user).setText(elem.username)
        myView.findViewById<TextView>(R.id.score_user).setText(elem.highscore.toString())

        val storageRef = Account.storage.reference
        val path : String = "images/"+ elem.uid
        val  imagesRef: StorageReference? = storageRef.child(path)
        val imageView = myView.findViewById<ImageView>(R.id.avatar_element)
        val localFile = File.createTempFile("images", "jpg")
        imagesRef?.getFile(localFile)?.addOnSuccessListener {
            // Local temp file has been created
            imageView.setImageBitmap((BitmapFactory.decodeFile(localFile.path)))
        }?.addOnFailureListener {
            // Handle any errors
            null
        }
        layout!!.addView(myView)
    }
}


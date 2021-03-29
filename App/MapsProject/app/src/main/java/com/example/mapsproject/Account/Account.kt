package com.example.mapsproject.Account

import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.mapsproject.R
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

object Account {

    var auth: FirebaseAuth= Firebase.auth
    var user: FirebaseUser? = null
    var storage = Firebase.storage
    val db = Firebase.firestore




    public fun logOut(){
        user = null
    }

    public fun signIn(email: String, password: String,saveInfo:Boolean,context: Context){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("myTag", "signInWithEmail:success")
                    user = auth.currentUser
                    //if checked -> save access credentials
                    if(saveInfo) {
                        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
                        val editor = sharedPref?.edit()
                        editor?.putString(R.string.email_key.toString(), email)
                        editor?.putString(R.string.password_key.toString(), password)
                        editor?.apply()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("myTag", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun createAccount(email: String, password: String,username:String,saveInfo:Boolean,context:Context) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("myTag", "createUserWithEmail:success")
                    user = auth.currentUser
                    if(saveInfo) {
                        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
                        val editor = sharedPref?.edit()
                        editor?.putString(R.string.email_key.toString(), email)
                        editor?.putString(R.string.password_key.toString(), password)
                        editor?.apply()
                    }
                    updateUserName(username,context)
                    uploadUserToFireStore(0)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("myTag", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

            }

        // [END create_user_with_email]
    }


    public fun getUserName():String{
        if(user==null){
            Log.i("myTag","no user")
            return ""
        }
        var name:String=""
        user?.let{
            name = user?.displayName.toString()
        }
        return name
    }

    public fun updateUserName(name:String,context:Context){
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()
        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task->
            if(task.isSuccessful) {
                Log.i("myTag", "User profile updated")
                Toast.makeText(context, "User profile updated", Toast.LENGTH_SHORT).show()

            }
        }
    }

    public fun getUserID(): String {
        if(user==null){
            Log.i("myTag","no user")
            return ""
        }

        var uid:String=""
        user?.let{
            uid = user!!.uid.toString()
        }
        return uid
    }

    public fun getUserEmail():String{
        if(user==null){
            Log.i("myTag","no user")
            return ""
        }
        var mail:String=""
        user?.let{
            mail = user?.email.toString()
        }
        return mail
    }

    public fun updateUserEmail(email:String,context:Context){

        user!!.updateEmail(email).addOnCompleteListener { task->
            if(task.isSuccessful) {
                Log.i("myTag", "User email updated")
                Toast.makeText(context, "User email updated", Toast.LENGTH_SHORT).show()

            }
        }
    }


    public fun uploadUserToFireStore(highscore:Int) {
        val u =User(getUserID(),getUserName(),getUserEmail(),highscore )
        db.collection("users").document(getUserID()).set(u)
            .addOnSuccessListener { Log.d("myTag", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("myTag", "Error writing document", e) }

    }

    public fun getHighScore(): Int {
        var hs = 0
        db.collection("users").document(getUserID()).get()
            .addOnSuccessListener {    document->
                Log.d("myTag", "DocumentSnapshot successfully downloaded!")
                val u = document.toObject<User>()
                hs = u!!.highscore!!
            }
            .addOnFailureListener { e -> Log.w("myTag", "Error downloading document", e) }
        return hs
    }

    //get top 3 users by highscore
    public fun getLeaderboard(i:Int):List<User>{
        val list = mutableListOf<User>()
        val usersRef = db.collection("users")
        usersRef.orderBy("highscore", Query.Direction.DESCENDING).limit(i.toLong()).get()
                .addOnSuccessListener {    results->
                    Log.d("myTag", "QuerySnapshot successfully executed!")
                    for(document in results){
                        val u = document.toObject<User>()
                        list.add(u)
                    }

                }
                .addOnFailureListener { e -> Log.w("myTag", "Error executing query", e) }
        return list
    }

}
package com.example.mapsproject.Account

import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.example.mapsproject.R
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object Account {

    var auth: FirebaseAuth= Firebase.auth
    var user: FirebaseUser? = null


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

    fun createAccount(email: String, password: String,saveInfo:Boolean,context:Context) {
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
}
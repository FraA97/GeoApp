package com.example.mapsproject

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mapsproject.Account.Account.auth
import com.example.mapsproject.Account.Account.signIn
import com.example.mapsproject.Account.Account.user
import com.example.mapsproject.Account.LoginActivity
import com.example.mapsproject.Account.SignupActivity
import com.example.mapsproject.Multiplayer.MultiplayerActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)


        //if email and password are saved, try firebase login
        if(sharedPref?.contains("email") == true && sharedPref?.contains("password")) {
            //get email and password

            val email = sharedPref?.getString("email", "")
            val password = sharedPref?.getString("password", "")

            Log.i("myTag", "try login with email: " + email + ", pws: " + password)

            signIn(email!!, password!!,false,this)
            if(user != null){
               val intent = Intent(this, StartGameActivity::class.java)
               startActivity(intent)
            }
        }
        else{
            Log.i("myTag", "no password or email stored")
        }

        setContentView(R.layout.activity_main)

        //launch login activity
        findViewById<Button>(R.id.log_in_btn).setOnClickListener{ view->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //launch signup activity
        findViewById<Button>(R.id.sign_up_btn).setOnClickListener{ view->
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }



    }


}
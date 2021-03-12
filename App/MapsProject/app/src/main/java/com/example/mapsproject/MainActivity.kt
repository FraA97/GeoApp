package com.example.mapsproject

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mapsproject.Account.LoginActivity
import com.example.mapsproject.Account.SignupActivity
import com.example.mapsproject.Multiplayer.MultiplayerActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*auth = FirebaseAuth.getInstance()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPref?.edit()

        //if email and password are saved, try firebase login
        if(sharedPref?.contains("email") == true && sharedPref?.contains("password")) {
            //get email and password

            val email = sharedPref?.getString("email", "")
            val password = sharedPref?.getString("password", "")

            Log.i("myTag", "try login with email: " + email + ", pws: " + password)

            //access to firebase
            auth.signInWithEmailAndPassword(email.toString(), password.toString()).addOnCompleteListener(this, OnCompleteListener { task ->
                //task is succesfull
                if (task.isSuccessful) {

                    /*val userid = Firebase.auth.currentUser?.uid
                    Log.i("myTag","user id: "+userid)
*/
                    Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()

                    //if checked -> save access credentials
                    val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
                    val editor = sharedPref?.edit()
                    editor?.putString("email", email)
                    editor?.putString("password", password)
                    editor?.apply()

                    //go to Start Activity
                    val intent = Intent(this, StartGameActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    //show error with Toast
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                }
            })
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

    */

     /*test multiplayer*/
        val intent = Intent(this, StartGameActivity::class.java)
        startActivity(intent)
    }



}
package com.example.mapsproject

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class LoginActivity:AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var checked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        //Switch changes value of var checked
        findViewById<Switch>(R.id.switch_li).setOnCheckedChangeListener { buttonView, isChecked ->
            checked = isChecked
        }

        //log in buttno onClickListener
        findViewById<Button>(R.id.login_btn).setOnClickListener { view->

            //get email and password
            val email = findViewById<EditText>(R.id.email_entry_li).text.toString()
            val password = findViewById<EditText>(R.id.psw_entry_li).text.toString()

            //access to firebase
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                //task is succesfull
                if(task.isSuccessful) {
                    Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()

                    //if checked -> save access credentials
                    if(checked) {
                        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
                        val editor = sharedPref?.edit()
                        editor?.putString("email", email)
                        editor?.putString("password", password)
                        editor?.apply()
                    }

                    //go to Start Activity
                    val intent = Intent(this, StartGameActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    //show error with Toast
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                }
            })
        }

        //password forgotten string onCliCkListener
        findViewById<TextView>(R.id.psw_forgot).setOnClickListener{view->
            //launch ForgotPassword Activity
            val intent = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}
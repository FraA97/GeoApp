package com.example.mapsproject.Account

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mapsproject.Account.Account.auth
import com.example.mapsproject.Account.Account.user
import com.example.mapsproject.Account.Account.signIn
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity:AppCompatActivity() {
    private var checked = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Switch changes value of var checked
        findViewById<Switch>(R.id.switch_li).setOnCheckedChangeListener { buttonView, isChecked ->
            checked = isChecked
        }

        //log in button onClickListener
        findViewById<Button>(R.id.login_btn).setOnClickListener { view->
            //get email and password
            val email = findViewById<EditText>(R.id.email_entry_li).text.toString()
            val password = findViewById<EditText>(R.id.psw_entry_li).text.toString()
            signIn(email,password,checked,this )
            if(user!=null)
                updateUI()


        }

        //password forgotten string onCliCkListener
        findViewById<TextView>(R.id.psw_forgot).setOnClickListener{ view->
            //launch ForgotPassword Activity
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun updateUI() {
        //go to Start Activity
        val intent = Intent(this, StartGameActivity::class.java)
        startActivity(intent)
        finish()
    }
}
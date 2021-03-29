package com.example.mapsproject.Account

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mapsproject.Account.Account.createAccount
import com.example.mapsproject.Account.Account.user
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity

class SignupActivity:AppCompatActivity() {
    private var checked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        //Switch changes value of var checked
        findViewById<Switch>(R.id.switch_su).setOnCheckedChangeListener { buttonView, isChecked ->
            checked = isChecked
        }


        //signup button OnClickListener
        findViewById<Button>(R.id.submit_btn).setOnClickListener { view->

            val username:String = findViewById<EditText>(R.id.username_entry).text.toString()

            val email:String = findViewById<EditText>(R.id.email_entry_su).text.toString()

            val psw1 = findViewById<EditText>(R.id.psw1_entry_su).text.toString()
            val psw2 = findViewById<EditText>(R.id.psw2_entry_su).text.toString()
            //check passwords are equal
            //if not show error via Toast and return
            if (psw1 =="" || psw2 =="" || psw1 != psw2) {
                Toast.makeText(this, "Passwords Have to Be Identical", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val password = psw1

            createAccount(email,password,username,checked,this)
            Handler(Looper.getMainLooper()).postDelayed({updateUI()}, 500L)


        }

    }


    private fun updateUI() {
        if(user!= null) {
            //goto Start Activity
            val intent = Intent(this, StartGameActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            Handler(Looper.getMainLooper()).postDelayed({updateUI()}, 500L)

        }
    }
}
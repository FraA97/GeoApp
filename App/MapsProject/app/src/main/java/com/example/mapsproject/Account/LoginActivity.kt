package com.example.mapsproject.Account

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mapsproject.Account.Account.auth
import com.example.mapsproject.Account.Account.user
import com.example.mapsproject.Account.Account.signIn
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.MainActivity
import com.example.mapsproject.R
import com.example.mapsproject.Settings.Settings
import com.example.mapsproject.StartGameActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*


class LoginActivity:AppCompatActivity() {
    private var checked = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //configure language
        val myLocale = Locale(MultiPlayerServerConf.language)
        val res: Resources = resources
        val dm: DisplayMetrics = res.getDisplayMetrics()
        val conf: Configuration = res.getConfiguration()
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
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
            if(email!= "" && password!= "") {
                signIn(email, password, checked, this)
            }
        }
        //password forgotten string onCliCkListener
        findViewById<TextView>(R.id.psw_forgot).setOnClickListener{ view->
            //launch ForgotPassword Activity
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
        Handler(Looper.getMainLooper()).postDelayed({updateUI()}, 500L)

    }


    private fun updateUI() {
        if(user!= null) {
            //go to Start Activity
            val intent = Intent(this, StartGameActivity::class.java)
            startActivity(intent)
            finish()
        }
        else
            Handler(Looper.getMainLooper()).postDelayed({updateUI()}, 500L)
    }

    //inflate menu_main
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_start , menu)
        return true
    }

    //handle menu actions
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings->{
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()

            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        finish()
        startActivity(i)
    }
}
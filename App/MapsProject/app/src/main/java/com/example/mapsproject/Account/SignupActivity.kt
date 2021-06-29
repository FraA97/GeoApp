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
import com.example.mapsproject.Account.Account.createAccount
import com.example.mapsproject.Account.Account.user
import com.example.mapsproject.MainActivity
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.R
import com.example.mapsproject.Settings.Settings
import com.example.mapsproject.StartGameActivity
import java.util.*

class SignupActivity:AppCompatActivity() {
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
            if (psw1.length < 8) {
                Toast.makeText(this, "Password must have at least 8 characters", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(username.length< 4){
                Toast.makeText(this, "Username must contain at least 4 characters", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(!email.contains("@")){
                Toast.makeText(this, "This is not a valid email", Toast.LENGTH_LONG).show()
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